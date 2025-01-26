document.addEventListener('DOMContentLoaded', function () {
    const userId = localStorage.getItem('userId');
    const receiverId = new URLSearchParams(window.location.search).get('receiverId');

    if (!userId) {
        alert('用户未登录');
        window.location.href = '/login.html';
        return;
    }

    if (!receiverId) {
        alert('未指定聊天对象');
        return;
    }

    const chatMessages = document.getElementById('chatMessages');

    // 获取双方的聊天记录
    fetch(`/chat/history/${userId}/${receiverId}`)
        .then(response => response.json())
        .then(data => {
            if (data.code === 1) {
                data.data.forEach(message => {
                    const messageElement = document.createElement('div');
                    messageElement.classList.add('message');
                    messageElement.classList.add(message.senderId === parseInt(userId) ? 'sender' : 'receiver');
                    messageElement.textContent = `${message.senderName}: ${message.content}`;
                    chatMessages.appendChild(messageElement);
                });
            } else {
                alert('获取聊天记录失败: ' + (data.msg || '未知错误'));
            }
        })
        .catch(error => {
            console.error('拉取聊天记录失败:', error);
            alert('网络错误，请重试');
        });

    // 初始化WebSocket连接
    const socket = new WebSocket(`ws://localhost:8080/chat?userId=${userId}`);

    socket.onopen = () => {
        console.log('WebSocket连接已建立');
    };

    socket.onmessage = (event) => {
        const message = JSON.parse(event.data);
        const messageElement = document.createElement('div');
        messageElement.classList.add('message');
        messageElement.classList.add(message.senderId === parseInt(userId) ? 'sender' : 'receiver');
        messageElement.textContent = `${message.senderName}: ${message.content}`;
        chatMessages.appendChild(messageElement);
        chatMessages.scrollTop = chatMessages.scrollHeight;
    };

    socket.onclose = () => {
        console.log('WebSocket连接已关闭');
    };

    // 绑定表单提交事件
    const sendMessageForm = document.getElementById('sendMessageForm');
    if (sendMessageForm) {
        sendMessageForm.addEventListener('submit', function (e) {
            e.preventDefault();
            const messageContent = document.getElementById('messageContent').value;

            const chatMessage = {
                senderId: parseInt(userId),
                senderName: localStorage.getItem('username'),
                receiverId: parseInt(receiverId),
                content: messageContent
            };

            fetch('/chat/send', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(chatMessage)
            })
                .then(response => response.json())
                .then(data => {
                    if (data.code === 1) {
                        // 发送成功后，刷新消息列表
                        window.location.reload();
                    } else {
                        alert('消息发送失败: ' + (data.msg || '未知错误'));
                    }
                })
                .catch(error => {
                    alert('网络错误，请重试');
                });
        });
    }
});