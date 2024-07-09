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
        atualizarCarrinho();
    });

    document.getElementById('closeOrder').addEventListener('click', function () {
        document.getElementById('orderSidebar').classList.remove('open');
    });

    // Abrir modal de detalhes do produto ao clicar em um card de produto

    // Abrir modal de detalhes do produto ao clicar em um card de produto
    document.getElementById('productList').addEventListener('click', function (event) {
        if (event.target.classList.contains('details-btn')) {
            const card = event.target.closest('.card');
            const productId = card.getAttribute('data-id'); // Obtém o ID do produto do atributo data-id do card
            window.location.href = `teste.html?id=${productId}`;
        }
    });

    // Adicionar ao carrinho ao clicar no botão dentro do modal
    document.getElementById('addToCartBtn').addEventListener('click', function () {
        const productId = document.getElementById('productId').value; // Obtém o ID do produto do modal
        adicionarAoCarrinho(1, productId); // Adiciona 1 unidade do produto ao carrinho
        const productModal = new bootstrap.Modal(document.getElementById('productModal'));
        productModal.hide();
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
                    const a = document.createElement('a');
                    a.href = '#';
                    a.textContent = categoria.nome;
                    li.appendChild(a);
                    categoryList.appendChild(li);
                });
            })
            .catch(error => {
                console.error('Erro ao carregar categorias:', error);
                // Tratar o erro de forma adequada, como exibir uma mensagem para o usuário
            });
    }

    // Função para carregar produtos por categoria
    function carregarProdutosPorCategoria(categoriaNome) {
        fetch(`http://localhost:8080/produtos/categoria/${categoriaNome}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Não foi possível carregar os produtos.');
                }
                return response.json();
            })
            .then(data => {
                const productList = document.getElementById('productList');
                productList.innerHTML = '';

                if (data.length === 0) {
                    const noProductsMessage = document.getElementById('noProductsMessage');
                    noProductsMessage.style.display = 'block';
                    noProductsMessage.innerHTML = `<p>Nenhum produto encontrado - ${categoriaNome}</p>`;
                    return;
                }

                data.forEach(produto => {
                    const card = document.createElement('div');
                    card.classList.add('card', 'col-lg-3', 'col-md-4', 'col-sm-6', 'mb-4');
                    card.setAttribute('data-id', produto.id); // Adiciona o ID do produto como um atributo data-id no card
                    card.innerHTML = `
                        <img src="http://localhost:8080/produtos/imagem/${produto.id}" class="card-img-top" alt="${produto.nome}">
                        <div class="card-body">
                            <h5 class="card-title">${produto.nome}</h5>
                            <p class="card-text">Preço: R$ ${produto.preco.toFixed(2)}</p>
                            <button class="btn btn-primary details-btn" data-bs-toggle="modal" data-bs-target="#productModal">Detalhes</button>
                        </div>`;
                    productList.appendChild(card);
                });

                document.getElementById('categoriaSelecionada').textContent = `Produtos - ${categoriaNome}`;
            })
            .catch(error => {
                console.error('Erro ao carregar produtos:', error);
                // Tratar o erro de forma adequada, como exibir uma mensagem para o usuário
                const productList = document.getElementById('productList');
                const noProductsMessage = document.getElementById('noProductsMessage');
                productList.innerHTML = '';
                noProductsMessage.style.display = 'block';
                noProductsMessage.innerHTML = `<p>Não foi possível carregar produtos da categoria ${categoriaNome}.</p>`;
            });
    }

    // Função para carregar detalhes do produto
    function carregarDetalhesDoProduto(productId) {
        fetch(`http://localhost:8080/produtos/${productId}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Não foi possível carregar os detalhes do produto.');
                }
                return response.json();
            })
            .then(data => {
                // Preenche os elementos do modal com as informações do produto
                document.getElementById('productImage').src = `http://localhost:8080/produtos/imagem/${data.id}`;
                document.getElementById('productName').textContent = data.nome;
                document.getElementById('productDescription').textContent = `Descrição: ${data.descricao}`;
                document.getElementById('productPrice').textContent = `Preço: R$ ${data.preco.toFixed(2)}`;

                // Adiciona o ID do produto a um campo oculto no modal
                document.getElementById('productId').value = data.id;

                // Exibe o modal
                const productModal = new bootstrap.Modal(document.getElementById('productModal'));
                productModal.show();
            })
            .catch(error => {
                console.error('Erro ao carregar detalhes do produto:', error);
                // Tratar o erro de forma adequada, como exibir uma mensagem para o usuário
            });
    }

});
