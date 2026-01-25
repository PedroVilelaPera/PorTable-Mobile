import { getDatabase } from '../database/db.js';

export const getSheetsByUser = async (req, res) => {
    const { usuario_id } = req.params;
    
    try {
        const db = await getDatabase();
        const sheets = await db.all('SELECT * FROM fichas WHERE usuario_id = ?', [usuario_id]);

        res.json(sheets);
    } catch (error) {
        res.status(500).json({ error: "✖ Erro ao buscar fichas." });
    }
};