import { getDatabase } from '../database/db.js';

export const login = async (req, res) => {
    const { email, senha } = req.body;
    const db = await getDatabase();

    try {
        const user = await db.get('SELECT * FROM usuarios WHERE email = ? AND senha = ?', [email, senha]);

        if (user) {
            // Retorna o ID e nome para o android
            res.json({ 
                success: true, 
                userId: user.id, 
                nome: user.nome 
            });
        } else {
            res.status(401).json({ success: false, message: "✖ Email ou senha incorretos." });
        }
    } catch (error) {
        res.status(500).json({ success: false, message: "✖ Erro no servidor." });
    }
};