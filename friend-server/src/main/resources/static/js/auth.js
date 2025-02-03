document.addEventListener('DOMContentLoaded', function () {
    const loginForm = document.getElementById('loginForm');
    const registerForm = document.getElementById('registerForm');
    const sendCodeBtn = document.getElementById('sendCodeBtn');

    if (loginForm) {
        loginForm.addEventListener('submit', async function (e) {
            e.preventDefault();
            const email = document.getElementById('email').value;
            const password = document.getElementById('password').value;

            try {
                const response = await fetch('/user/login', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ userEmail: email, userPassword: password }),
                });
                const data = await response.json();
                if (response.ok) {
                    // 存储用户信息到 localStorage
                    localStorage.setItem('userId', data.data.userId);
                    localStorage.setItem('username', data.data.userName);
                    window.location.href = `/chat.html?userId=${data.data.userId}`;
                } else {
                    document.getElementById('error').textContent = data.msg || '登录失败';
                }
            } catch (error) {
                document.getElementById('error').textContent = '网络错误，请重试';
            }
        });
    }

    if (sendCodeBtn) {
        sendCodeBtn.addEventListener('click', async function () {
            const email = document.getElementById('email').value;
            if (!email) {
                alert('请输入邮箱');
                return;
            }

            try {
                const response = await fetch(`/user/sendVerificationCode?email=${email}`);
                if (response.ok) {
                    alert('验证码已发送');
                } else {
                    alert('验证码发送失败');
                }
            } catch (error) {
                alert('网络错误，请重试');
            }
        });
    }

    if (registerForm) {
        registerForm.addEventListener('submit', async function (e) {
            e.preventDefault();
            const email = document.getElementById('email').value;
            const username = document.getElementById('username').value;
            const password = document.getElementById('password').value;
            const code = document.getElementById('code').value;

            try {
                const response = await fetch('/user/regist', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({
                        userEmail: email,
                        userName: username,
                        userPassword: password,
                        userCode: code,
                    }),
                });
                const data = await response.json();
                if (response.ok) {
                    // 存储用户信息到 localStorage
                    localStorage.setItem('userId', data.data.userId);
                    localStorage.setItem('username', data.data.userName);
                    window.location.href = `/chat.html?userId=${data.data.userId}`;
                } else {
                    document.getElementById('error').textContent = data.msg || '注册失败';
                }
            } catch (error) {
                document.getElementById('error').textContent = '网络错误，请重试';
            }
        });
    }
});