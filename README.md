Esta API surgiu como uma ideia de projeto para a matéria de Atividade Extensionista: Tecnologia aplicada a inclusão digital, 
cujo objetivo era apresentar uma solução que agregasse valor à comunidade por meio da tecnologia. Pensando nisso, segui o caminho
de apresentar uma solução que permitisse aos protetores de animais (independentes ou pequenas ongs) um melhor controle das doações 
recebidas, adoções registradas e voluntários ativos.

## Arquitetura

Neste projeto é utilizada a arquitetura em camadas, onde a Repository Layer é responsável pelas operações de banco de dados, a Service 
Layer concentra a lógica de negócio e a Controller Layer atua como interface REST. Essa abordagem facilita a execução de testes e a 
realização de manutenções futuras, pois cada camada possui um papel bem definido.

## Funcionalidades

**Autenticação via Usuário/Senha e geração de token JWT com Spring Security:** A escolha desta forma de autenticação se deu por ser simples 
para os usuários com a parte de login, mas ainda assim fornecer uma camada de segurança através da geração de um token JWT para validar 
a sessão. Quando o usuário insere as credenciais corretas, é gerado um token que expirará após um certo período de tempo.


<img width="1300" height="350" alt="Geração token" src="https://github.com/user-attachments/assets/15b17227-3e56-4c2a-898e-7c0be1d0a24f" />


**Proteção de endpoints e métodos com Spring Security:** Defini que a autorização será baseada em 3 roles distintas (ADMIN, VOLUNTARIO E PADRAO) e 
organizei a autorização de acesso aos endpoints baseados nelas. 


<img width="1300" height="350" alt="SecurityFilterChain" src="https://github.com/user-attachments/assets/fe665635-8211-4e70-a3aa-4cd1f056beaa" />


Também utilizei o @PreAutorize para implementar segurança em métodos em que apenas um usuário com uma role específica pode ter acesso.


<img width="1300" height="350" alt="@PreAutorize" src="https://github.com/user-attachments/assets/67e9e5e8-08a4-4790-b932-1480ef2f1d38" />


**DTOs de Request/Response:** Adotei o padrão DTO para garantir que nenhuma informação sensível, como por exemplo a senha de um usuário, ficasse 
acessível para um terceiro. Além disso, é uma boa prática do Spring utilizar DTOs para manipulação de dados ao invés das entidades para garantir
uma manutenção mais fácil da aplicação. Para as validações, utilizei a biblioteca Jakarta Validation em conjunto com expressões regulares (regex).


<img width="950" height="415" alt="Dto" src="https://github.com/user-attachments/assets/8c71981d-0e0b-4905-b6e0-4918e4560874" />


**Mappers para conversão de dados:** Juntamente ao padrão DTO implementei classes de mappers, com o objetivo de facilitar a conversão entidade/DTO
e vice-versa.


<img width="1000" height="415" alt="Mapper" src="https://github.com/user-attachments/assets/e58450b3-2757-4a25-ba9a-4d533e9754d4" />


**Validações personalizadas:** Foram implementadas validações na Service Layer de acordo com regras de negócio específicas. Por exemplo, para que um 
animal possa receber um pedido de adoção, ele deve estar com status "disponível". Além disso, é verificado se o usuário logado já possui um pedido 
de adoção em aberto para o mesmo animal, evitando a duplicidade de solicitações.


<img width="1300" height="530" alt="validações" src="https://github.com/user-attachments/assets/c73a6762-9ab0-4d0f-a8d4-91221dbf4493" />


**Operações de CRUD:** Criei operações de CRUD para todas as entidades, implementando as regras de negócio pertinentes na Service Layer,
como por exemplo a regra de que só um usuário com role VOLUNTARIO possui permissão para analisar um pedido de adoção e realizar uma 
operação de UPDATE do status da adoção.


<img width="1173" height="231" alt="crud" src="https://github.com/user-attachments/assets/7264d3b3-70e6-40dd-ab91-698bb34ababa" />


**Tratamento global de exceções:** O tratamento de exceções está centralizado em um @ControllerAdvice GlobalExceptionHandler,
que captura os erros mais comuns e retorna respostas mais amigáveis ao usuário.


<img width="1000" height="700" alt="exception" src="https://github.com/user-attachments/assets/cba7eaca-9f1e-436c-9589-80b6068f53d4" />


**Documentação dos endpoints:** A documentação dos endpoints foi realizada através do Swagger, com a possibilidade de testar as requisições
diretamente no navegador e com exemplos já estruturados para maior facilidade de entendimento.


<img width="1824" height="833" alt="Swagger" src="https://github.com/user-attachments/assets/f20d72ee-484a-4531-a14d-92649b284ed6" />

Novas funcionalidades serão implementadas no futuro, bem como as já existentes serão expandidas para melhor atender os usuários finais.

## Tecnologias Utilizadas

- Java 21;
- Spring Boot 3.5.4;
- Spring Security;
- JWT 0.11.5
- Lombok;
- PostgreSQL;
- JUnit e Mockito;
- Swagger OpenAPI.

