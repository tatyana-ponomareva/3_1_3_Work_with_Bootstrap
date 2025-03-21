function loadUserInfo() {
    fetch('/user/showUser', { credentials: 'same-origin' })
        .then(response => response.json())
        .then(user => {
            document.getElementById('currentUsername').textContent = user.username;
            const roles = user.roles.map(role => role.role).join(', ');
            document.getElementById('currentRoles').textContent = roles;

            const tableBody = document.getElementById('userInfoTableBody');
            const row = `
                <tr>
                    <td>${user.id}</td>
                    <td>${user.username}</td>
                    <td>${user.password}</td>
                    <td>${roles}</td>
                </tr>
            `;
            tableBody.innerHTML = row;
        })
        .catch(error => console.error('Error loading user data:', error));
}

// Вызываем загрузку данных при загрузке страницы
document.addEventListener('DOMContentLoaded', loadUserInfo);