import { useEffect, useState } from 'react';
import { Category } from 'types/category';
import { requestBackend } from 'util/requests';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faEdit, faTrash } from '@fortawesome/free-solid-svg-icons';

import { Link } from 'react-router-dom';
import DeleteConfirmationModal from 'components/DeleteConfirmationModal';
import { AxiosRequestConfig } from 'axios';
import { toast } from 'react-toastify';
import Spinner from 'components/Spinner';

import './styles.css';

const List = () => {
  const [selectCategories, setSelectCategories] = useState<Category[]>([]);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedItem, setSelectedItem] = useState<Category | null>(null);
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    getCategories();
  }, []);

  const getCategories = () => {
    requestBackend({ url: '/categories' }).then((response) => {
      setSelectCategories(response.data.content);
    });
  };

  const handleDelete = (id: number) => {
    const config: AxiosRequestConfig = {
      method: 'DELETE',
      url: `/categories/${id}`,
      withCredentials: true,
    };

    requestBackend(config)
      .then(() => {
        toast.success('Categoria excluída com sucesso!');
        getCategories();
      })
      .catch((error) => {
        console.error('Erro ao excluir categoria:', error);
        toast.error('Erro ao excluir categoria. Por favor, tente novamente.');
      })
      .finally(() => {
        setIsLoading(false);
      });
  };

  const handleOpenModal = (item: any) => {
    const categoryToDelete = item as Category;
    setSelectedItem(categoryToDelete);
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setSelectedItem(null);
    setIsModalOpen(false);
  };

  const handleDeleteItem = () => {
    setIsLoading(true);

    if (selectedItem?.id !== undefined) {
      handleDelete(selectedItem?.id);
    }
    setSelectedItem(null);
    setIsModalOpen(false);
  };

  return (
    <>
      {isLoading ? (
        <Spinner />
      ) : (
        <div className="category-crud-container table-responsive">
          <div className="">
            <table className="custom-table table-bordered">
              <thead>
                <tr>
                  <th style={{ width: '50px' }}>#</th>
                  <th>Nome</th>
                  <th className="action-itens">Ação</th>
                </tr>
              </thead>
              <tbody>
                {selectCategories.map((item) => (
                  <tr key={item.id}>
                    <td>{item.id}</td>
                    <td>{item.name}</td>
                    <div className="action-itens">
                      <td>
                        <Link to={`/admin/categories/${item.id}`}>
                          <FontAwesomeIcon
                            icon={faEdit}
                            className="icon icon-blue"
                            title="Editar"
                          />
                        </Link>
                        <FontAwesomeIcon
                          icon={faTrash}
                          onClick={() => handleOpenModal(item)}
                          className="icon icon-red"
                          title="Excluir"
                        />
                      </td>
                    </div>
                  </tr>
                ))}
              </tbody>
            </table>
            <DeleteConfirmationModal
              isOpen={isModalOpen}
              onClose={handleCloseModal}
              onConfirm={handleDeleteItem}
            />
          </div>
          <div className="category-list-container">
            <Link to="/admin/categories/create">
              <button className="btn btn-primary text-white btn-category-add">
                ADICIONAR
              </button>
            </Link>
          </div>
        </div>
      )}
    </>
  );
};

export default List;
