# PorTable App 🎲📜
O PorTable é uma solução mobile para gestão de fichas de RPG, que substitui o papel por uma interface dinâmica sincronizada em tempo real com um servidor dedicado. O projeto foca em oferecer fluidez para o jogador e integridade para os dados.
<div>
  <img src="https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white" />
  <img src="https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" />
  <img src="https://img.shields.io/badge/node.js-6DA55F?style=for-the-badge&logo=nodespace&logoColor=white" />
  <img src="https://img.shields.io/badge/sqlite-%2307405E.svg?style=for-the-badge&logo=sqlite&logoColor=white" />
</div>

## 🚀 Funcionalidades
- Gestão de Personagens: Criação, edição e exclusão de fichas de RPG de forma intuitiva.
- Interface Multi-Abas: Organização de atributos, perícias e habilidades utilizando ViewPager2 e Fragments.
- Sincronização Automática (Debounce): O aplicativo detecta pausas na digitação e envia as atualizações para o servidor automaticamente, garantindo que o progresso nunca seja perdido.
- Autenticação e Sessão: Sistema de login com controle de estado global para garantir que o usuário acesse apenas suas próprias fichas.
- Persistência Robusta: Armazenamento centralizado em banco de dados relacional, permitindo que os dados persistam mesmo após reinstalações do app.

## 🛠 Tecnologias Utilizadas
- Linguagem: Kotlin
- Interface: ViewPager2, TabLayout e ViewBinding.
- Rede: Retrofit com conversor GSON para consumo de API REST.
- Assincronia: Handler e Looper para controle do temporizador de salvamento (debounce).
- Backend: Node.js com Framework Express.
- Banco de Dados: SQLite para persistência relacional.

## 📦 Estrutura do Projeto

```Plaintext
android-app/
├── activities/ # Fluxo de navegação e controle de sessão
├── fragments/  # Interfaces específicas da ficha (Info, Perícias, Habilidades)
├── network/    # Configuração do Retrofit e definições da API
└── model/      # Data classes e modelos de resposta

backend/
├── routes/      # Endpoints de autenticação e fichas
├── controllers/ # Lógica de manipulação de dados no SQLite
└── database/    # Configuração do banco de dados relacional
```

## ⚙️ Como Rodar
1. Backend:
    - Navegue até a pasta ```backend.```
    - Execute ```npm install``` para as dependências.
    - Inicie o servidor com ```npm run dev```.

2. Android:
    - Abra a pasta ```android-app``` no Android Studio.
    - Certifique-se de que a ```BASE_URL``` no ```RetrofitClient.kt``` aponta para o endereço correto do seu servidor.
    - Sincronize o Gradle e execute o projeto.
