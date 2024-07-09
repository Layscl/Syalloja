document.addEventListener('DOMContentLoaded', function () {
    const API_URL = 'http://localhost:8080'; // Altere para a URL da sua API

    // Função para carregar categorias nos selects
    function carregarCategorias(selectId) {
        axios.get(`${API_URL}/categorias`)
            .then(response => {
                const categorias = response.data;
                const select = document.getElementById(selectId);
                select.innerHTML = '<option value="" selected disabled>Selecione a categoria</option>';
                categorias.forEach(categoria => {
                    const option = `<option value="${categoria.id}">${categoria.nome}</option>`;
                    select.innerHTML += option;
                });
            })
            .catch(error => {
                console.error('Erro ao carregar categorias:', error);
                alert('Houve um erro ao carregar as categorias. Por favor, tente novamente.');
            });
    }

    // Carregar categorias nos selects de adicionar e editar produto
    carregarCategorias('categoriaProdutoModal');
    carregarCategorias('categoriaProdutoModalEditar');

    // Função para carregar lista de produtos
    function carregarListaProdutos() {
        axios.get(`${API_URL}/produtos`)
            .then(response => {
                const produtos = response.data;
                const listaProdutosContainer = document.getElementById('listaProdutosContainer');
                listaProdutosContainer.innerHTML = '';

                produtos.forEach(produto => {
                    const tr = document.createElement('tr');
                    tr.innerHTML = `
                        <td>${produto.id}</td>
                        <td>${produto.nome}</td>
                        <td>${produto.descricao}</td>
                        <td>${produto.preco}</td>
                        <td>${produto.quantidade}</td>
                        <td>${produto.categoria.nome}</td>
                        <td>
                            <button class="btn btn-sm btn-primary btn-editar" data-bs-toggle="modal"
                                data-bs-target="#modalEditarProduto" data-id="${produto.id}">
                                Editar
                            </button>
                            <button class="btn btn-sm btn-danger btn-excluir" data-id="${produto.id}">
                                Excluir
                            </button>
                        </td>
                    `;
                    listaProdutosContainer.appendChild(tr);
                });

                // Adicionar evento de clique nos botões de editar e excluir
                adicionarEventosBotoes();
            })
            .catch(error => {
                console.error('Erro ao carregar lista de produtos:', error);
                alert('Houve um erro ao carregar a lista de produtos. Por favor, tente novamente.');
            });
    }

    // Função para adicionar eventos de clique nos botões de editar e excluir
    function adicionarEventosBotoes() {
        const botoesEditar = document.querySelectorAll('.btn-editar');
        botoesEditar.forEach(botao => {
            botao.addEventListener('click', () => {
                const idProduto = botao.getAttribute('data-id');
                carregarDadosProdutoParaEdicao(idProduto);
            });
        });

        const botoesExcluir = document.querySelectorAll('.btn-excluir');
        botoesExcluir.forEach(botao => {
            botao.addEventListener('click', () => {
                const idProduto = botao.getAttribute('data-id');
                excluirProduto(idProduto);
            });
        });
    }

    // Carregar lista de produtos ao carregar a página
    carregarListaProdutos();

    // Função para carregar dados do produto para edição
    function carregarDadosProdutoParaEdicao(idProduto) {
        axios.get(`${API_URL}/produtos/${idProduto}`)
            .then(response => {
                const produto = response.data;
                document.getElementById('idProdutoModal').value = produto.id;
                document.getElementById('nomeProdutoModalEditar').value = produto.nome;
                document.getElementById('descricaoProdutoModalEditar').value = produto.descricao;
                document.getElementById('precoProdutoModalEditar').value = produto.preco;
                document.getElementById('quantidadeProdutoModalEditar').value = produto.quantidade;
                document.getElementById('categoriaProdutoModalEditar').value = produto.categoria.id;
            })
            .catch(error => {
                console.error('Erro ao carregar produto para edição:', error);
                alert('Houve um erro ao carregar o produto para edição. Por favor, tente novamente.');
            });
    }

    // Evento de envio do formulário de cadastrar produto
    document.getElementById('formAddProdutoModal').addEventListener('submit', function (event) {
        event.preventDefault();
        const formData = new FormData(this);

        // Convertendo FormData para objeto JSON
        const produtoData = {
            nome: formData.get('nomeProdutoModal'),
            descricao: formData.get('descricaoProdutoModal'),
            preco: formData.get('precoProdutoModal'),
            quantidade: formData.get('quantidadeProdutoModal'),
            categoria: {
                id: formData.get('categoriaProdutoModal')
            }
        };

        axios.post(`${API_URL}/produtos`, produtoData, {
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(() => {
                $('#modalAdicionarProduto').modal('hide');
                carregarListaProdutos();
                alert('Produto cadastrado com sucesso!');
                document.getElementById('formAddProdutoModal').reset();
            })
            .catch(error => {
                console.error('Erro ao adicionar produto:', error);
                alert('Houve um erro ao cadastrar o produto. Por favor, tente novamente.');
            });
    });


    // Evento de envio do formulário de editar produto
    document.getElementById('formEditarProdutoModal').addEventListener('submit', function (event) {
        event.preventDefault();
        const formData = new FormData(this);
        const idProduto = formData.get('idProdutoModal');

        // Convertendo FormData para objeto JSON
        const produtoData = {
            id: idProduto,
            nome: formData.get('nomeProdutoModalEditar'),
            descricao: formData.get('descricaoProdutoModalEditar'),
            preco: formData.get('precoProdutoModalEditar'),
            quantidade: formData.get('quantidadeProdutoModalEditar'),
            categoria: {
                id: formData.get('categoriaProdutoModalEditar')
            }
        };

        axios.put(`${API_URL}/produtos/${idProduto}`, produtoData, {
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(() => {
                $('#modalEditarProduto').modal('hide');
                carregarListaProdutos();
                alert('Produto editado com sucesso!');
            })
            .catch(error => {
                console.error('Erro ao editar produto:', error);
                alert('Houve um erro ao editar o produto. Por favor, tente novamente.');
            });
    });


    // Função para excluir um produto
    function excluirProduto(idProduto) {
        if (confirm('Tem certeza que deseja excluir este produto?')) {
            axios.delete(`${API_URL}/produtos/${idProduto}`)
                .then(() => {
                    carregarListaProdutos();
                    alert('Produto excluído com sucesso!');
                })
                .catch(error => {
                    console.error('Erro ao excluir produto:', error);
                    alert('Houve um erro ao excluir o produto. Por favor, tente novamente.');
                });
        }
    }
});