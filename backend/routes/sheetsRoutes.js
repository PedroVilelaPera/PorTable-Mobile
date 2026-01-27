import express from 'express';
import { getSheetsByUser, createSheet, deleteSheet , getSheetById , updateSheet} from '../controllers/sheetController.js';

const router = express.Router();

router.get('/:usuario_id', getSheetsByUser);
router.post('/', createSheet);             
router.delete('/:id', deleteSheet);
router.get('/id/:id', getSheetById);
router.put('/:id', updateSheet);

export default router;