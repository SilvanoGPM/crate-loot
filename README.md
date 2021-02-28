<p align="center" >
  Plugin básico de caixas configuráveis.
</p>

<p align="center" ><img src="Crate.png" /></p>


## Colocando o plugin no servidor

- Baixe a última versão do ![plugin](https://github.com/SkyG0D/CrateLoot/releases/tag/1.0-SNAPSHOT).
- Adicione o plugin no diretório `plugins` de seu servidor.
- Inicie o servidor e teste o plugin com o seguinte comando: `/crates`.

## Criando uma nova caixa

Seguindo o passo a passo a seguir, você conseguirá registrar novas caixas.

- Pegue o item para ser a caixa.
- Troque o nome dele em uma bigorna (cores são aceitas).

![image](https://user-images.githubusercontent.com/59753526/109374451-210e2480-7894-11eb-8c7b-da8fb287d72f.png)

- Pegue os itens para a caixa (mínimo de 5 itens).
- Digite o comando `/crates` e clique em New crate.
- Clique no item selecionado para ser a caixa.
- Clique em Next.
- Adicione os itens no inventário que foi aberto.
- Feche o inventário e pronto, a caixa foi adicionada.

![create](https://user-images.githubusercontent.com/59753526/109413873-69aa0880-798e-11eb-9912-86adb765973e.gif)

## Pegando caixas registradas

Seguindo o passo a passo a seguir, você conseguirá listar e pegar caixas já registradas.

- Digite o comando `/crates` e clique em View all crates.
- Selecione a caixa que você deseja.

![list](https://user-images.githubusercontent.com/59753526/109413921-be4d8380-798e-11eb-862f-5bd592e74c25.gif)

## Removendo caixas

Seguindo o passo a passo a seguir, você conseguirá remover caixas.

- Digite o comando `/crates` e clique em Remove crate.
- Clique na caixa que você quer remover e confirme.

![remove](https://user-images.githubusercontent.com/59753526/109413944-dd4c1580-798e-11eb-945d-a3c8e69f23f1.gif)

## Alterando a configuração

Dentro do diretório `plugins` é criado um diretório chamado `CrateLoot`, esse diretório contém algumas configurações para o plugin funcionar corretamente. O arquivo `crates.yml` contém todas as caixas registradas, já o arquivo `config.yml` contém algumas configurações básicas, que podem ser alteradas.

### Propriedades do config.yml

Propriedade        | Utilização
------------------ | -------------------------
inventory-full     | Mensagem quando o inventário do jogador estiver lotado.
sorting            | Mensagem quando o jogador estiver abrindo uma crate.
timeout            | Tempo de espera para abrir as crates.
timeout-message    | Mensagem quando o jogador tentar utilizar a caixa ainda em cooldown.

## Contribuições

Para contribuir, por favor, abra um pull request.
