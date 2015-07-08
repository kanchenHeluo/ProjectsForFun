using Microsoft.Ecit.China.Tools.EcitAssistantRobot.Log;
using Microsoft.Ecit.China.Tools.EcitAssistantRobot.Shell;
using Microsoft.Lync.Model;
using Microsoft.Lync.Model.Conversation;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Microsoft.Ecit.China.Tools.EcitAssistantRobot.LyncShell
{
    internal class LyncShellImpl : IShell
    {
        #region IShell
        public bool IsReady
        {
            get { return _lyncClient != null && _lyncClient.State == ClientState.SignedIn; }
        }

        public void Start()
        {
            _logger.Write(TraceEventType.Information, "Starting Lync shell.");

            _conversations.Clear();
            lock (_messageQueue)
                _messageQueue.Clear();

            _logger.Write(TraceEventType.Verbose, "Killing Lync processes.");
            KillSideBySideLyncProcesses();

            _logger.Write(TraceEventType.Verbose, "Getting Lync client.");
            _lyncClient = LyncClient.GetClient(true);

            _logger.Write(TraceEventType.Verbose, "Initializing Lync client.");
            _taskFactory.FromAsync(_lyncClient.BeginInitialize, _lyncClient.EndInitialize, null).Wait();
            AddEventHandler(_lyncClient);
        }

        public void Stop()
        {
            _logger.Write(TraceEventType.Information, "Stopping Lync shell.");

            if (_lyncClient != null)
            {
                _logger.Write(TraceEventType.Verbose, "Shutting down Lync client.");
                RemoveEventHandler(_lyncClient);
                if (_lyncClient.State == ClientState.SignedIn)
                    _taskFactory.FromAsync(_lyncClient.BeginSignOut, _lyncClient.EndSignOut, null).Wait();
                _lyncClient = null;
            }

            _logger.Write(TraceEventType.Verbose, "Killing Lync processes.");
            KillSideBySideLyncProcesses();
        }

        public ShellMessage Read()
        {
            if (_lyncClient == null || _lyncClient.State != ClientState.SignedIn)
                throw new LyncClientNotSignedInException();

            if (_messageQueue.Count <= 0)
                return null;
            lock (_messageQueue)
                return _messageQueue.Dequeue();
        }

        public void Write(ShellMessage message)
        {
            if (_lyncClient == null || _lyncClient.State != ClientState.SignedIn)
                throw new LyncClientNotSignedInException();

            if (message.ConversationID == null || !_conversations.ContainsKey(message.ConversationID))
                return;

            var conversation = _conversations[message.ConversationID];
            var participant = conversation.SelfParticipant;
            if (participant.Modalities == null || !participant.Modalities.ContainsKey(ModalityTypes.InstantMessage))
                return;

            var imm = (InstantMessageModality)participant.Modalities[ModalityTypes.InstantMessage];
            var imsg = new LyncInstantMessage { Modality = imm, Content = message.Content };
            //debug font
            IDictionary<InstantMessageContentType, string> dct = new Dictionary<InstantMessageContentType, string>();
            dct[InstantMessageContentType.Html] = message.Content;
            imm.BeginSendMessage(dct, SendMessageComplete, imsg);
            //imm.BeginSendMessage(message.Content, SendMessageComplete, imsg);
        }

        private void SendMessageComplete(IAsyncResult ar)
        {
            var imsg = (LyncInstantMessage)ar.AsyncState;
            imsg.Modality.EndSendMessage(ar);

            _logger.Write(TraceEventType.Verbose, string.Format("Message sent.\t[{0}] {1}: {2}",
                imsg.Modality.Conversation.Properties[ConversationProperty.Id],
                imsg.Modality.Participant.Properties[ParticipantProperty.Name],
                imsg.Content));
        }

        public string AddConversation()
        {
            if (_lyncClient == null || _lyncClient.State != ClientState.SignedIn)
                throw new LyncClientNotSignedInException();

            var conversation = _lyncClient.ConversationManager.AddConversation();
            while (!_conversations.ContainsKey(conversation.Properties[ConversationProperty.Id].ToString())) ;

            return conversation.Properties[ConversationProperty.Id].ToString();
        }

        public void AddParticipant(string conversationID, string user)
        {
            if (conversationID == null || !_conversations.ContainsKey(conversationID))
                return;

            var conversation = _conversations[conversationID];

            var contact = _lyncClient.ContactManager.GetContactByUri(user);

            var sb = new StringBuilder();
            var ppp = (List<object>)contact.GetContactInformation(ContactInformationType.InstantMessageAddresses);
            foreach (var p in ppp)
                sb.AppendLine(p.ToString());

            _logger.Write(TraceEventType.Warning, sb.ToString());


            conversation.AddParticipant(_lyncClient.ContactManager.GetContactByUri(user));
        }
        #endregion IShell

        /// <summary>
        /// 
        /// </summary>
        /// <param name="loginAddress">user@domain.com</param>
        /// <param name="password">***</param>
        /// <param name="logger">logger</param>
        internal LyncShellImpl(string loginAddress, string password, ILogger logger)
            : this(loginAddress, loginAddress, password, logger)
        {
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="loginAddress">user@domain.com</param>
        /// <param name="userName">user@domain.com -or- DOMAIN\user</param>
        /// <param name="password">***</param>
        /// <param name="logger">logger</param>
        internal LyncShellImpl(string loginAddress, string userName, string password, ILogger logger)
        {
            if (loginAddress == null)
                throw new ArgumentNullException("loginAddress");

            if (userName == null)
                throw new ArgumentNullException("userName");

            if (password == null)
                throw new ArgumentNullException("password");

            if (logger == null)
                throw new ArgumentNullException("logger");

            _loginAddress = loginAddress;
            _userName = userName;
            _password = password;
            _logger = logger;

            _taskFactory = new TaskFactory();
        }

        #region Lync Process
        private static void KillSideBySideLyncProcesses()
        {
            var procList = Process.GetProcessesByName("lync");

            foreach (var p in procList)
            {
                p.Kill();
            }

            while(true)
            {
                if (Process.GetProcessesByName("lync").Length == 0)
                    return;
            }
        }

        private void AddEventHandler(LyncClient lyncClient)
        {
            lyncClient.StateChanged += LyncClient_StateChanged;
            lyncClient.ConversationManager.ConversationAdded += ConversationManager_ConversationAdded;
            lyncClient.ConversationManager.ConversationRemoved += ConversationManager_ConversationRemoved;
        }

        private void RemoveEventHandler(LyncClient lyncClient)
        {
            lyncClient.StateChanged -= LyncClient_StateChanged;
            lyncClient.ConversationManager.ConversationAdded -= ConversationManager_ConversationAdded;
            lyncClient.ConversationManager.ConversationRemoved -= ConversationManager_ConversationRemoved;
        }

        private void AddEventHandler(Conversation conversation)
        {
            conversation.ParticipantAdded += Conversation_ParticipantAdded;
            conversation.ParticipantRemoved += Conversation_ParticipantRemoved;
        }

        private void RemoveEventHandler(Conversation conversation)
        {
            conversation.ParticipantAdded -= Conversation_ParticipantAdded;
            conversation.ParticipantRemoved -= Conversation_ParticipantRemoved;
        }

        private void AddEventHandler(Participant participant)
        {
            if (participant.IsSelf || participant.Modalities == null || !participant.Modalities.ContainsKey(ModalityTypes.InstantMessage))
                return;

            var imm = (InstantMessageModality)participant.Modalities[ModalityTypes.InstantMessage];
            imm.InstantMessageReceived += InstantMessageModality_InstantMessageReceived;
        }

        private void RemoveEventHandler(Participant participant)
        {
            if (participant.IsSelf || participant.Modalities == null || !participant.Modalities.ContainsKey(ModalityTypes.InstantMessage))
                return;

            var imm = (InstantMessageModality)participant.Modalities[ModalityTypes.InstantMessage];
            imm.InstantMessageReceived -= InstantMessageModality_InstantMessageReceived;
        }
        #endregion Lync Process

        #region Lync Event Handlers
        void LyncClient_StateChanged(object sender, ClientStateChangedEventArgs e)
        {
            _logger.Write(TraceEventType.Verbose, string.Format("LyncClient_StateChanged\t{0}->{1}", e.OldState, e.NewState));

            if (e.NewState == ClientState.SignedOut)
            {
                _lyncClient.BeginSignIn(_loginAddress, _userName, _password,
                    (ar) => _lyncClient.EndSignIn(ar), null);
            }

            if (e.NewState == ClientState.SignedIn)
            {
                if (_lyncClient.Self != null && _lyncClient.Self.Contact != null)
                    _lyncClient.Self.Contact.ContactInformationChanged += SelfContact_ContactInformationChanged;
            }
            else
            {
                if (_lyncClient.Self != null && _lyncClient.Self.Contact != null)
                    _lyncClient.Self.Contact.ContactInformationChanged -= SelfContact_ContactInformationChanged;
            }
        }

        void SelfContact_ContactInformationChanged(object sender, ContactInformationChangedEventArgs e)
        {
            if (_lyncClient != null && _lyncClient.State == ClientState.SignedIn &&
                e.ChangedContactInformation.Contains(ContactInformationType.Availability))
            {
                var self = (Contact)sender;
                var availability = (ContactAvailability)self.GetContactInformation(ContactInformationType.Availability);
                if (availability != ContactAvailability.Free)
                {
                    var items = new Dictionary<PublishableContactInformationType, object>
                    {
                        { PublishableContactInformationType.Availability, ContactAvailability.Free }
                    };
                    _lyncClient.Self.BeginPublishContactInformation(items,
                        ar => _lyncClient.Self.EndPublishContactInformation(ar), null);
                }
            }
        }

        void ConversationManager_ConversationAdded(object sender, ConversationManagerEventArgs e)
        {
            _logger.Write(TraceEventType.Verbose, string.Format("ConversationManager_ConversationAdded\t{0}",
                e.Conversation.Properties[ConversationProperty.Id]));

            AddEventHandler(e.Conversation);
            var cid = e.Conversation.Properties[ConversationProperty.Id].ToString();
            _conversations.Add(cid, e.Conversation);
        }

        void ConversationManager_ConversationRemoved(object sender, ConversationManagerEventArgs e)
        {
            _logger.Write(TraceEventType.Verbose, string.Format("ConversationManager_ConversationRemoved\t{0}",
                e.Conversation.Properties[ConversationProperty.Id]));

            RemoveEventHandler(e.Conversation);
            var cid = e.Conversation.Properties[ConversationProperty.Id].ToString();
            if (_conversations.ContainsKey(cid))
                _conversations.Remove(cid);
        }

        void Conversation_ParticipantAdded(object sender, ParticipantCollectionChangedEventArgs e)
        {
            _logger.Write(TraceEventType.Verbose, string.Format("Conversation_ParticipantAdded\t{0}", e.Participant.Properties[ParticipantProperty.Name]));
            AddEventHandler(e.Participant);
        }

        void Conversation_ParticipantRemoved(object sender, ParticipantCollectionChangedEventArgs e)
        {
            _logger.Write(TraceEventType.Verbose, string.Format("Conversation_ParticipantRemoved\t{0}", e.Participant.Properties[ParticipantProperty.Name]));
            RemoveEventHandler(e.Participant);
        }

        void InstantMessageModality_InstantMessageReceived(object sender, MessageSentEventArgs e)
        {
            var imm = (InstantMessageModality)sender;
            _logger.Write(TraceEventType.Verbose, string.Format("InstantMessageModality_InstantMessageReceived\t[{0}] {1}: {2}",
                imm.Conversation.Properties[ConversationProperty.Id], imm.Participant.Properties[ParticipantProperty.Name], e.Text));

            lock (_messageQueue)
                _messageQueue.Enqueue(new ShellMessage
                {
                    ConversationID = imm.Conversation.Properties[ConversationProperty.Id].ToString(),
                    ParticipantName = imm.Participant.Properties[ParticipantProperty.Name].ToString(),
                    Content = e.Text,

                    ParticipantUri = imm.Participant.Contact.Uri.StartsWith("sip:")
                        ? imm.Participant.Contact.Uri.Substring(4, imm.Participant.Contact.Uri.Length - 4)
                        : imm.Participant.Contact.Uri,
                });
        }
        #endregion Lync Event Handlers

        private readonly string _loginAddress;
        private readonly string _userName;
        private readonly string _password;
        private readonly ILogger _logger;
        private readonly TaskFactory _taskFactory;
        private readonly object _messageQueueLock = new object();

        private LyncClient _lyncClient;
        private Dictionary<string, Conversation> _conversations = new Dictionary<string, Conversation>();
        private Queue<ShellMessage> _messageQueue = new Queue<ShellMessage>();
    }
}
