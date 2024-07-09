// Exemplo de script para abrir o menu do usuário
const userIcon = document.getElementById('user-icon');
const userMenu = document.getElementById('user-menu');

userIcon.addEventListener('click', () => {
    userMenu.classList.toggle('show');
});

// Exemplo de script para abrir o carrinho como sidebar
const openCartSidebar = document.getElementById('open-cart-sidebar');
const sidebar = document.querySelector('.sidebar');

openCartSidebar.addEventListener('click', () => {
    sidebar.classList.toggle('open');
});