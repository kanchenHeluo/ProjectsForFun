using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace Microsoft.Ecit.China.Tools.EcitAssistantRobot.LyncShell
{
    public partial class LyncCredentialDialog : Form
    {
        public LyncCredentialDialog()
        {
            InitializeComponent();
        }

        public string UserAddress
        {
            get { return txtUserAddress.Text; }
        }

        public string Password
        {
            get { return txtPassword.Text; }
        }
    }
}
