import express from 'express';
import cors from 'cors';
import { setupDb } from './database/db.js';
import authRoutes from './routes/authRoutes.js';
import sheetsRoutes from './routes/sheetsRoutes.js';

const app = express();

app.use(cors());
app.use(express.json());

// Rotas de autenticação
app.use('/auth', authRoutes);
app.use('/fichas', sheetsRoutes);

const PORT = process.env.PORT || 3000; 

const startServer = async () => {
      // Inicializando banco de dados
      await setupDb().catch(err => {
          console.error("✖ Erro ao configurar o banco de dados:", err);
          process.exit(1); 
      });

      // Rota de teste
      app.get('/', (req, res) => {
          res.send('Servidor PorTable Mobile - Status OK!');
      });

      app.listen(PORT, () => {
          console.log(`Servidor rodando em: http://localhost:${PORT}`);
      });
};

startServer();