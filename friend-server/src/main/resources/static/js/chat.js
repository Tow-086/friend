document.addEventListener('DOMContentLoaded', function () {
    const userId = localStorage.getItem('userId');

    if (!userId) {
        alert('用户未登录');
        window.location.href = '/login.html';
        return;
    }

    const chatMessages = document.getElementById('chatMessages');
    const loadMoreButton = document.getElementById('loadMoreMessages');
    const receiverIdInput = document.getElementById('receiverId');
    let offset = 0; // 用于分页，每次加载更多的消息时增加这个值
    const limit = 10; // 每次加载的消息数量

    // 加载更多消息的函数
    function loadMoreMessages() {
        const receiverId = receiverIdInput.value;
        if (!receiverId) {
            alert('未指定聊天对象');
            return;
        }

        console.log('loadMoreMessages called with offset:', offset);
        fetch(`/chat/history?senderId=${userId}&receiverId=${receiverId}&offset=${offset}&limit=${limit}`)
            .then(response => response.json())
            .then(data => {
                if (data.code === 200) {
                    const messages = data.data;
                    const chatMessagesDiv = document.getElementById('chatMessages');
                    messages.forEach(message => {
                        const messageElement = document.createElement('div');
                        messageElement.classList.add('message');
                        messageElement.classList.add(message.senderId === parseInt(userId) ? 'sender' : 'receiver');
                        messageElement.textContent = `${message.senderName}: ${message.content}`;
                        chatMessagesDiv.appendChild(messageElement);
                    });
                    offset += messages.length; // 更新offset以便下次加载更多消息
                    if (messages.length < limit) {
                        loadMoreButton.style.display = 'none'; // 如果返回的消息少于请求的数量，说明没有更多消息了
                    }
                } else {
                    alert('加载消息失败: ' + data.message);
                }
            })
            .catch(error => {
                console.error('Error loading more messages:', error);
                alert('加载消息时发生错误');
            });
    }

    // 初始化加载第一页消息
    loadMoreButton.addEventListener('click', function () {
        console.log('Load more button clicked');
        loadMoreMessages();
    });

    // 监听滚动事件，加载更多消息
    chatMessages.addEventListener('scroll', function () {
        if (chatMessages.scrollTop === 0) {
            loadMoreMessages();
        }
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
            const receiverId = receiverIdInput.value;

            if (!receiverId) {
                alert('未指定聊天对象');
                return;
            }

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
