import { Product } from "types/product";
import './styles.css';

type Props = {
  isOpen: boolean;
  onClose: () => void;
  onConfirm: () => void;
};

const DeleteConfirmationModal = ({ isOpen, onClose, onConfirm }: Props) => {
  if (!isOpen) {
    return null;
  }

  const handleConfirm = () => {
    onConfirm();
    onClose();
  };

  return (
    <>
    <div className="modal-overlay base-card">
      <div className="modal-container-products">
        <h2>Confirmar Exclusão</h2>
        <p>Tem certeza que deseja deletar?</p>
        <div className="modal-buttons">
          <button onClick={handleConfirm}>Sim</button>
          <button onClick={onClose}>Cancelar</button>
        </div>
      </div>
    </div>
    </>
  );
};

export default DeleteConfirmationModal;
