import { useEffect, useState } from 'react';
import { Category } from 'types/category';
import { requestBackend } from 'util/requests';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faEdit, faTrash } from '@fortawesome/free-solid-svg-icons';

import './styles.css';

const List = () => {
  const [selectCategories, setSelectCategories] = useState<Category[]>([]);

  useEffect(() => {
    requestBackend({ url: '/categories' }).then((response) => {
      setSelectCategories(response.data.content);
    });
  }, []);

  const handleEdit = (id: number) => {
    // Lógica para editar o item com o ID fornecido
  };

  const handleDelete = (id: number) => {
    // Lógica para excluir o item com o ID fornecido
  };

  return (
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
                    {/* Ícones de editar e excluir */}
                    <FontAwesomeIcon
                      icon={faEdit}
                      onClick={() => handleEdit(item.id)}
                      className="icon icon-blue"
                      title="Editar"
                    />
                    <FontAwesomeIcon
                      icon={faTrash}
                      onClick={() => handleDelete(item.id)}
                      className="icon icon-blue"
                      title="Excluir"
                    />
                  </td>
                </div>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default List;
