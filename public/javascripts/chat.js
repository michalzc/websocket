function ChatApplication() {

    var self = this;

    var loginPanel = document.getElementById("loginPanel");
    var chatPanel = document.getElementById("chatPanel");
    var loginButton = document.getElementById("performLogin");
    var loginName = document.getElementById("loginName");
    var chatContent = document.getElementById("chatContent");
    var chatLine = document.getElementById("chatLine");
    var loginForm = document.getElementById("loginForm");
    var chatForm = document.getElementById("chatForm");
    var sendButton = document.getElementById("chatSend");

    this.performLogin = function (e) {
        var name = loginName.value;
        loginName.value = "";
        if (name) {
            self.login(name);
        }
        return false;
    };

    this.performSendLine = function(e) {
        var chatMsg = chatLine.value;
        chatLine.value = "";
        self.sendLine(chatMsg);

        return false;
    }

    this.init = function () {
        self.showLoginForm();

        loginButton.onclick = self.performLogin;
        loginForm.onsubmit = self.performLogin;

        chatForm.onsubmit = self.performSendLine;
        sendButton.onclick = self.performSendLine;
    };

    this.showLoginForm = function () {
        loginPanel.style.display = "block";
        loginName.focus();
    };

    this.hideLoginForm = function () {
        loginPanel.style.display = "none";
    };

    this.showChatPanel = function () {
        chatPanel.style.display = "block";
        chatLine.focus();
    };

    this.hideChatPanel = function () {
        chatPanel.style.display = "none";
    };

    this.displayMessage = function (msg) {
        var text = chatContent.value;
        text += "\n" + msg.data;
        chatContent.value = text;
    };

    this.login = function (name) {
        self.webSocket = new WebSocket("ws://localhost:9000/chat/socket?name=" + encodeURIComponent(name));

        self.webSocket.onopen = function (ev) {
            self.hideLoginForm();
            self.showChatPanel();
        };

        self.webSocket.onerror = function (ev) {
            self.hideChatPanel();
            self.showLoginForm();
        };

        self.webSocket.onmessage = function (msg) {
            self.displayMessage(msg);
        };
    }

    this.sendLine = function(msg) {
        if(self.webSocket) {
            self.webSocket.send(msg);
        }
    }
}

var chatApplication = new ChatApplication();

window.onload = function (e) {
    chatApplication.init();
};