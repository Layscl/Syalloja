document.addEventListener('DOMContentLoaded', function () {
    carregarCategorias(); // Carrega as categorias ao carregar a página

    // Adiciona evento de clique nas categorias
    document.getElementById('categoryList').addEventListener('click', function (event) {
        if (event.target.tagName === 'A') {
            const categoriaNome = event.target.textContent.trim();
            carregarProdutosPorCategoria(categoriaNome);
        }
    });

    // Abrir e fechar pedido
    document.getElementById('order-icon').addEventListener('click', function () {
        document.getElementById('orderSidebar').classList.add('open');
    });
    document.getElementById('closeOrder').addEventListener('click', function () {
        document.getElementById('orderSidebar').classList.remove('open');
    });

    // Abrir modal de detalhes do produto ao clicar em um card de produto
    document.getElementById('productList').addEventListener('click', function (event) {
        if (event.target.classList.contains('details-btn')) {
            const card = event.target.closest('.card');
            const productId = card.getAttribute('data-id'); // Obtém o ID do produto do atributo data-id do card
            carregarDetalhesDoProduto(productId);
        }
    });

    // Adicionar ao pedido ao clicar no botão dentro do modal
    document.getElementById('addToOrderBtn').addEventListener('click', function () {
        const productId = document.getElementById('productId').value; // Obtém o ID do produto do modal
        adicionarAoPedido(1, productId); // Adiciona 1 unidade do produto ao pedido
        const productModal = bootstrap.Modal.getInstance(document.getElementById('productModal'));
        productModal.hide();
    });

    // Finalizar Pedido
    document.getElementById('checkoutBtn').addEventListener('click', function () {
        finalizarPedido(); // Chama a função para finalizar o pedido
    });
});

// Função para carregar as categorias
function carregarCategorias() {
    fetch('http://localhost:8080/categorias')
        .then(response => {
            if (!response.ok) {
                throw new Error('Não foi possível carregar as categorias.');
            }
            return response.json();
        })
        .then(data => {
            const categoryList = document.getElementById('categoryList');
            categoryList.innerHTML = '';
            data.forEach(categoria => {
                const li = document.createElement('li');
                const link = document.createElement('a');
                link.href = '#';
                link.textContent = categoria.nome;
                li.appendChild(link);
                categoryList.appendChild(li);
            });
        })
        .catch(error => {
            console.error('Erro ao carregar categorias:', error);
            // Trate o erro de forma adequada, exibindo uma mensagem ao usuário, por exemplo.
        });
}

// Função para carregar produtos de uma categoria específica
function carregarProdutosPorCategoria(categoriaNome) {
    fetch(`http://localhost:8080/produtos/categoria/${encodeURIComponent(categoriaNome)}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Não foi possível carregar os produtos.');
            }
            return response.json();
        })
        .then(data => {
            const productList = document.getElementById('productList');
            productList.innerHTML = '';
            data.forEach(produto => {
                const card = document.createElement('div');
                card.classList.add('card', 'col-md-4', 'mb-4');
                card.setAttribute('data-id', produto.id); // Atributo data-id para identificar o produto
                card.innerHTML = `
                    <img src="" class="card-img-top" alt="${produto.nome}"> <!-- Corrigido para deixar o src vazio -->
                    <div class="card-body">
                        <h5 class="card-title">${produto.nome}</h5>
                        <p class="card-text">${produto.descricao}</p>
                        <p class="card-price">R$ ${produto.preco.toFixed(2)}</p>
                        <button class="btn btn-primary details-btn">Detalhes</button>
                    </div>
                `;
                productList.appendChild(card);
            });
            document.getElementById('categoriaSelecionada').textContent = `Produtos - ${categoriaNome}`;
        })
        .catch(error => {
            console.error('Erro ao carregar produtos:', error);
            // Trate o erro de forma adequada, exibindo uma mensagem ao usuário, por exemplo.
        });
}

// Função para carregar os detalhes de um produto
function carregarDetalhesDoProduto(productId) {
    fetch(`http://localhost:8080/produtos/${productId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Não foi possível carregar os detalhes do produto.');
            }
            return response.json();
        })
        .then(produto => {
            const modalBody = document.getElementById('modalBody');
            modalBody.innerHTML = `
                <div class="text-center">
                    <img src="" alt="${produto.nome}" class="img-fluid mb-3"> <!-- Corrigido para deixar o src vazio -->
                </div>
                <h5>${produto.nome}</h5>
                <p>${produto.descricao}</p>
                <p class="text-success">R$ ${produto.preco.toFixed(2)}</p>
                <input type="hidden" id="productId" value="${produto.id}">
            `;

            const productModal = new bootstrap.Modal(document.getElementById('productModal'));
            productModal.show();
        })
        .catch(error => {
            console.error('Erro ao carregar os detalhes do produto:', error);
            // Trate o erro de forma adequada, exibindo uma mensagem ao usuário, por exemplo.
        });
}

// Função para adicionar produto ao pedido
function adicionarAoPedido(quantidade, productId) {
    const orderItems = document.getElementById('orderItems');
    const productItem = `
        <li>${quantidade}x Produto ${productId}</li>
    `;
    orderItems.insertAdjacentHTML('beforeend', productItem);
}

// Função para finalizar o pedido
function finalizarPedido() {
    // Implemente conforme necessário
    alert('Implemente a função para finalizar o pedido.');
}

// Função para finalizar o pedido no backend
function finalizarPedido(idProduto, idCliente) {
    // Monta o objeto pedido com os detalhes necessários
    const pedido = {
        cliente: {
            id: idCliente
        },
        produtos: [
            {
                id: idProduto
                // Outros detalhes do produto, se necessário
            }
            // Você pode adicionar mais produtos se necessário
        ]
        // Outros detalhes do pedido, se necessário
    };

    fetch('http://localhost:8080/pedidos/finalizar', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(pedido),
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Erro ao finalizar o pedido.');
            }
            return response.json();
        })
        .then(data => {
            // Aqui você pode lidar com a resposta do backend, como redirecionar para a página de pagamento, etc.
            console.log('Pedido finalizado com sucesso:', data);
            // Exemplo de redirecionamento para uma página de pagamento
            window.location.href = 'pagamento.html';
        })
        .catch(error => {
            console.error('Erro ao finalizar o pedido:', error);
            // Trate o erro de forma adequada, exibindo uma mensagem ao usuário, por exemplo.
        });
}