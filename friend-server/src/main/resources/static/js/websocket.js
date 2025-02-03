document.addEventListener('DOMContentLoaded', function () {
    const userId = localStorage.getItem('userId');

    if (!userId) {
        alert('用户未登录');
        window.location.href = '/login.html';
        return;
    }

    const chatMessages = document.getElementById('chatMessages');

    // 初始化WebSocket连接
    const socket = new WebSocket(`ws://localhost:8080/chat?userId=${userId}`);

    socket.onopen = () => {
        console.log('WebSocket连接已建立');
        // 发送登录消息到服务器
        const loginMessage = {
            type: 'login', // 消息类型
            userId: parseInt(userId) // 用户ID
        };
        socket.send(JSON.stringify(loginMessage));
    };

    socket.onmessage = (event) => {
        const message = JSON.parse(event.data);
        const messageElement = document.createElement('div');
        messageElement.classList.add('message');
        messageElement.classList.add(message.senderId === parseInt(userId) ? 'sender' : 'receiver');
        messageElement.textContent = `${message.senderName}: ${message.content}`;
        chatMessages.appendChild(messageElement);
    };

    socket.onclose = () => {
        console.log('WebSocket连接已关闭');
    };

    // 发送聊天消息
    document.getElementById('sendMessageForm').addEventListener('submit', function (e) {
        e.preventDefault();
        const receiverId = document.getElementById('receiverId').value;
        const messageContent = document.getElementById('messageContent').value;

        const chatMessage = {
            type: 'chat', // 消息类型
            senderId: parseInt(userId),
            senderName: localStorage.getItem('username'),
            receiverId: parseInt(receiverId),
            content: messageContent
        };

        socket.send(JSON.stringify(chatMessage));
    });
});