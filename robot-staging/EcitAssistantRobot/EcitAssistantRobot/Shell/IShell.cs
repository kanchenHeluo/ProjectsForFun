using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Microsoft.Ecit.China.Tools.EcitAssistantRobot.Shell
{
    /// <summary>
    /// Represents a shell, which is used to interactive with user.
    /// </summary>
    public interface IShell
    {
        /// <summary>
        /// Gets a value, indicates whether the shell is ready for use.
        /// For example, a Lync shell is ready when it's initialized and signed in.
        /// </summary>
        bool IsReady { get; }

        /// <summary>
        /// Start and initialize the shell.
        /// </summary>
        void Start();

        /// <summary>
        /// Stop and shutdown the shell.
        /// </summary>
        void Stop();

        /// <summary>
        /// Read a piece of message from the shell.
        /// </summary>
        /// <returns>The message read from the shell.</returns>
        ShellMessage Read();

        /// <summary>
        /// Write a message to the shell, with context information.
        /// </summary>
        /// <param name="message">The message to write.</param>
        void Write(ShellMessage message);

        /// <summary>
        /// Create a new conversation.
        /// </summary>
        /// <returns>Conversation ID.</returns>
        string AddConversation();

        /// <summary>
        /// Add a participant to a conversation.
        /// </summary>
        /// <param name="conversationID">Conversation ID.</param>
        /// <param name="user">User identity, in form of 'user@domain.com'.</param>
        void AddParticipant(string conversationID, string user);
    }
}
