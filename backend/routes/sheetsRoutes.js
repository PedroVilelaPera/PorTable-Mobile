import express from 'express';
import { getSheetsByUser } from '../controllers/sheetController.js';

const router = express.Router();

router.get('/:usuario_id', getSheetsByUser);

export default router;