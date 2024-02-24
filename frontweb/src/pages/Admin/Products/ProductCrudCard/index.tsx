import './styles.css';

import ProductPrice from 'components/ProductPrice';
import { Product } from 'types/product';
import CategoryBadge from '../CategoryBadge';
import { Link } from 'react-router-dom';
import { AxiosRequestConfig } from 'axios';
import { requestBackend } from 'util/requests';
import { useState } from 'react';
import { toast } from 'react-toastify';

import DeleteConfirmationModal from 'components/DeleteConfirmationModal';
import Spinner from 'components/Spinner';

type Props = {
  product: Product;
  onDelete: Function;
};

const ProductCrudCard = ({ product, onDelete }: Props) => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isLoading, setIsLoading] = useState(false);

  const handleDelete = (productId: number) => {
    const config: AxiosRequestConfig = {
      method: 'DELETE',
      url: `/products/${productId}`,
      withCredentials: true,
    };

    requestBackend(config)
      .then(() => {
        toast.success('Produto excluído com sucesso!');
        onDelete();
      })
      .catch((error) => {
        console.error('Erro ao excluir produto:', error);
        toast.error('Erro ao excluir produto. Por favor, tente novamente.');
      })
      .finally(() => {
        setIsLoading(false); 
      });
  };
  const handleConfirmDelete = () => {
    setIsLoading(true);
    handleDelete(product.id);
    setIsModalOpen(false);
  };

  const handleDeleteButtonClick = () => {
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
  };

  return (
    <>
      {isLoading ? (
        <Spinner />
      ) : (
        <div className="base-card product-crud-card">
          <DeleteConfirmationModal
            isOpen={isModalOpen}
            onClose={handleCloseModal}
            onConfirm={handleConfirmDelete}
            product={product}
          />
          <div className="product-crud-card-top-container">
            <img src={product.imgUrl} alt={product.name} />
          </div>
          <div className="product-crud-card-description">
            <div className="product-crud-card-bottom-container">
              <h6>{product.name}</h6>
              <ProductPrice price={product.price} />
            </div>
            <div className="product-crud-categories-container">
              {product.categories.map((category) => (
                <CategoryBadge name={category.name} key={category.id} />
              ))}
            </div>
          </div>
          <div className="product-crud-card-buttons-container">
            <button
              onClick={handleDeleteButtonClick}
              className="btn btn-outline-danger product-crud-card-button product-crud-card-button-first"
            >
              EXCLUIR
            </button>
            <Link to={`/admin/products/${product.id}`}>
              <button className="btn btn-outline-secondary product-crud-card-button">
                EDITAR
              </button>
            </Link>
          </div>
        </div>
      )}
    </>
  );
};

export default ProductCrudCard;
