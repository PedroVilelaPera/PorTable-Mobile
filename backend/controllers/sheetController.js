import { getDatabase } from '../database/db.js';

export const getSheetsByUser = async (req, res) => {
    const { usuario_id } = req.params;
    
    try {
        const db = await getDatabase();
        const sheets = await db.all('SELECT * FROM fichas WHERE usuario_id = ?', [usuario_id]);

        res.json(sheets);
        console.log(`✔ Fichas do usuário ${usuario_id} buscadas com sucesso.`);
    } catch (error) {
        res.status(500).json({ error: "✖ Erro ao buscar fichas." });
        console.log("✖ Erro ao buscar fichas:", error);
    }
};

export const createSheet = async (req, res) => {
    const { usuario_id, dados_json } = req.body;
    try {
        const db = await getDatabase();
        const result = await db.run(
            'INSERT INTO fichas (usuario_id, dados_json) VALUES (?, ?)',
            [usuario_id, dados_json]
        );
        res.status(201).json({ id: result.lastID, usuario_id, dados_json });
        console.log("✔ Ficha criada com sucesso.");
    } catch (error) {
        res.status(500).json({ error: "✖ Erro ao criar ficha." });
        console.log("✖ Erro ao criar ficha:", error);
    }
};

export const deleteSheet = async (req, res) => {
    const { id } = req.params;
    try {
        const db = await getDatabase();
        await db.run('DELETE FROM fichas WHERE id = ?', [id]);
        res.json({ message: "✔ Ficha eliminada com sucesso." });
        console.log(`✔ Ficha ${id} eliminada com sucesso.`);
    } catch (error) {
        res.status(500).json({ error: "✖ Erro ao eliminar ficha." });
        console.log("✖ Erro ao eliminar ficha:", error);
    }
};

export const getSheetById = async (req, res) => {
    const { id } = req.params;
    try {
        const db = await getDatabase();
        const sheet = await db.get('SELECT * FROM fichas WHERE id = ?', [id]);

        if (sheet) {
            res.json(sheet);
            console.log(`✔ Ficha ${id} buscada com sucesso.`);
        } else {
            res.status(404).json({ error: "✖ Ficha não encontrada." });
            console.log(`✖ Ficha ${id} não encontrada.`);
        }
    } catch (error) {
        res.status(500).json({ error: "✖ Erro ao buscar ficha." });
        console.log("✖ Erro ao buscar ficha:", error);
    }
};

export const updateSheet = async (req, res) => {
    const { id } = req.params;
    const { dados_json } = req.body;
    
    try {
        const db = await getDatabase();
        
        // Executa a atualização baseada no ID único da ficha
        const result = await db.run(
            'UPDATE fichas SET dados_json = ? WHERE id = ?',
            [dados_json, id]
        );

        if (result.changes > 0) {
            console.log(`[LOG] Ficha ${id} atualizada com sucesso.`);
            res.json({ message: "✔ Ficha atualizada com sucesso." });
        } else {
            res.status(404).json({ error: "✖ Ficha não encontrada para atualização." });
        }
    } catch (error) {
        console.error("✖ Erro ao atualizar ficha:", error);
        res.status(500).json({ error: "✖ Erro interno ao atualizar no banco." });
    }
};