import sqlite3 from 'sqlite3';
import { open } from 'sqlite';

export async function getDatabase() {
    return open({
        filename: './database.db',
        driver: sqlite3.Database
    });
}

export async function setupDb() {
    const db = await getDatabase();
    
    // Tabela de usuarios
    await db.exec(`
        CREATE TABLE IF NOT EXISTS usuarios (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            nome TEXT NOT NULL,
            email TEXT UNIQUE NOT NULL,
            senha TEXT NOT NULL
        )
    `);

    // Tabela de fichas
    await db.exec(`
        CREATE TABLE IF NOT EXISTS fichas (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            usuario_id INTEGER NOT NULL,
            dados_json TEXT NOT NULL,
            FOREIGN KEY (usuario_id) REFERENCES usuarios (id)
        )
    `);

    // Insere um usuário padrão se não existir
    await db.run(`
        INSERT OR IGNORE INTO usuarios (id, nome, email, senha) 
        VALUES (?, ?, ?, ?)`, 
        [1, 'Admin', 'admin@email.com', '1234']
    );
    
    console.log("✔ Banco de dados pronto.");
}