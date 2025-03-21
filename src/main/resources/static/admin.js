// $(document).ready(function() {
//     // Функция загрузки всех пользователей
//     function loadUsers() {
//         fetch('/admin/users')
//             .then(response => response.json())
//             .then(users => {
//                 const tableBody = $('#usersTableBody');
//                 tableBody.empty();
//                 users.forEach(user => {
//                     const roles = user.roles.map(r => r.role).join(', ');
//                     const row = `
//                         <tr>
//                             <td>${user.id}</td>
//                             <td>${user.username}</td>
//                             <td>${user.password}</td>
//                             <td>${roles}</td>
//                             <td>
//                                 <button class="btn btn-info btn-sm edit-btn" data-id="${user.id}">Edit</button>
//                             </td>
//                             <td>
//                                 <button class="btn btn-danger btn-sm delete-btn" data-id="${user.id}">Delete</button>
//                             </td>
//                         </tr>
//                     `;
//                     tableBody.append(row);
//                 });
//             })
//             .catch(error => console.error('Error loading users:', error));
//     }
//
//     // Загрузка списка пользователей при загрузке страницы
//     loadUsers();
//
//     // Обработчик для открытия модального окна редактирования
//     $(document).on('click', '.edit-btn', function() {
//         const userId = $(this).data('id');
//         fetch(`/admin/edit/${userId}`)
//             .then(response => response.json())
//             .then(user => {
//                 $('#editUserId').val(user.id);
//                 $('#editUsername').val(user.username);
//                 $('#editPassword').val(user.password);
//                 const roles = user.roles.map(r => r.role).join(', ');
//                 $('#editRoles').val(roles);
//                 const editModal = new bootstrap.Modal(document.getElementById('editUserModal'));
//                 editModal.show();
//             })
//             .catch(error => console.error('Error fetching user data:', error));
//     });
//
//     // Отправка формы редактирования без перезагрузки страницы
//     $('#editUserForm').on('submit', function(e) {
//         e.preventDefault();
//         const userId = $('#editUserId').val();
//         const updatedUser = {
//             username: $('#editUsername').val(),
//             password: $('#editPassword').val()
//         };
//         const roles = $('#editRoles').val();
//         fetch(`/admin/update/${userId}?roles=${encodeURIComponent(roles)}`, {
//             method: 'POST',
//             headers: { 'Content-Type': 'application/json' },
//             body: JSON.stringify(updatedUser)
//         })
//             .then(response => {
//                 if (response.ok) {
//                     // Закрываем модальное окно и обновляем таблицу
//                     const modalEl = document.getElementById('editUserModal');
//                     const modal = bootstrap.Modal.getInstance(modalEl);
//                     modal.hide();
//                     loadUsers();
//                 } else {
//                     alert('Error updating user');
//                 }
//             })
//             .catch(error => console.error('Error updating user:', error));
//     });
//
//     // Открытие модального окна для удаления
//     $(document).on('click', '.delete-btn', function() {
//         const userId = $(this).data('id');
//         $('#deleteUserId').text(userId);
//         $('#confirmDeleteBtn').data('id', userId);
//         const deleteModal = new bootstrap.Modal(document.getElementById('deleteUserModal'));
//         deleteModal.show();
//     });
//
//     // Обработчик подтверждения удаления без перезагрузки страницы
//     $('#confirmDeleteBtn').on('click', function() {
//         const userId = $(this).data('id');
//         fetch(`/admin/delete/${userId}`, { method: 'GET' })
//             .then(response => {
//                 if (response.ok) {
//                     const modalEl = document.getElementById('deleteUserModal');
//                     const modal = bootstrap.Modal.getInstance(modalEl);
//                     modal.hide();
//                     loadUsers();
//                 } else {
//                     alert('Error deleting user');
//                 }
//             })
//             .catch(error => console.error('Error deleting user:', error));
//     });
//
//     // (Опционально) Загрузка данных текущего пользователя для отображения в Navbar
//     fetch('/user/showUser')
//         .then(response => response.json())
//         .then(user => {
//             $('#currentUsername').text(user.username);
//             const roles = user.roles.map(r => r.role).join(', ');
//             $('#currentRoles').text(roles);
//         })
//         .catch(error => console.error('Error loading current user:', error));
// });

$(document).ready(function(){
    // Функция загрузки всех пользователей
    function loadUsers(){
        $.ajax({
            url: "/admin/users",
            method: "GET",
            success: function(users){
                let tbody = $("#usersTableBody");
                tbody.empty();
                $.each(users, function(index, user){
                    // Получаем роли пользователя в виде строки (убираем префикс ROLE_)
                    let roles = user.roles.map(function(role){
                        return role.role.replace("ROLE_", "");
                    }).join(", ");
                    let row = `<tr>
                         <td>${user.id}</td>
                         <td>${user.username}</td>
                         <td>${user.password}</td>
                         <td>${roles}</td>
                         <td><button class="btn btn-primary btn-sm editUserBtn" data-id="${user.id}">Edit</button></td>
                         <td><button class="btn btn-danger btn-sm deleteUserBtn" data-id="${user.id}">Delete</button></td>
                     </tr>`;
                    tbody.append(row);
                });
            },
            error: function(xhr){
                alert("Ошибка загрузки пользователей: " + xhr.responseText);
            }
        });
    }
    loadUsers();

    // Обработчик отправки формы для добавления нового пользователя
    $("#newUserForm").submit(function(e){
        e.preventDefault();
        let username = $("#newUsername").val();
        let password = $("#newPassword").val();
        let rolestring = $("#newRoles").val();

        let newUser = {
            username: username,
            password: password
        };

        $.ajax({
            url: "/admin/users?rolestring=" + encodeURIComponent(rolestring),
            method: "POST",
            contentType: "application/json",
            data: JSON.stringify(newUser),
            success: function(data){
                alert("Пользователь успешно создан");
                $("#newUserForm")[0].reset();
                loadUsers();
            },
            error: function(xhr){
                alert("Ошибка при создании пользователя: " + xhr.responseText);
            }
        });
    });

    $(document).on("click", ".editUserBtn", function(){
        let id = $(this).data("id");
        $.ajax({
            url: "/admin/edit/" + id,
            method: "GET",
            success: function(user){
                $("#editUserId").val(user.id);
                $("#editUsername").val(user.username);
                $("#editPassword").val(user.password);
                let roles = user.roles.map(function(role){
                    return role.role.replace("ROLE_", "");
                }).join(", ");
                $("#editRoles").val(roles);
                $("#editUserModal").modal("show");
            },
            error: function(xhr){
                alert("Ошибка получения данных пользователя: " + xhr.responseText);
            }
        });
    });

    $("#editUserForm").submit(function(e){
        e.preventDefault();
        let id = $("#editUserId").val();
        let username = $("#editUsername").val();
        let password = $("#editPassword").val();
        let roles = $("#editRoles").val(); // роли, введённые через запятую

        let updatedUser = {
            username: username,
            password: password
        };

        $.ajax({
            url: "/admin/update/" + id + "?roles=" + encodeURIComponent(roles),
            method: "POST",
            contentType: "application/json",
            data: JSON.stringify(updatedUser),
            success: function(data){
                alert("Пользователь успешно обновлён");
                $("#editUserModal").modal("hide");
                loadUsers();
            },
            error: function(xhr){
                alert("Ошибка обновления пользователя: " + xhr.responseText);
            }
        });
    });

    $(document).on("click", ".deleteUserBtn", function(){
        let id = $(this).data("id");
        $("#deleteUserId").text(id);
        $("#confirmDeleteBtn").data("id", id);
        $("#deleteUserModal").modal("show");
    });

    $("#confirmDeleteBtn").click(function(){
        let id = $(this).data("id");
        $.ajax({
            url: "/admin/delete/" + id,
            method: "GET",
            success: function(){
                alert("Пользователь успешно удалён");
                $("#deleteUserModal").modal("hide");
                loadUsers();
            },
            error: function(xhr){
                alert("Ошибка удаления пользователя: " + xhr.responseText);
            }
        });
    });
});
