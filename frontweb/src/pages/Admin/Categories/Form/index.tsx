import { AxiosRequestConfig } from 'axios';
import { useState } from 'react';
import { useEffect } from 'react';
import { useForm, Controller } from 'react-hook-form';
import { useHistory, useParams } from 'react-router-dom';
import { Category } from 'types/category';
import { requestBackend } from 'util/requests';
import { toast } from 'react-toastify';

import './styles.css';

type UrlParams = {
  categoryId: string;
};

const Form = () => {
  const { categoryId } = useParams<UrlParams>();

  const isEditing = categoryId !== 'create';

  const history = useHistory();

  const {
    register,
    handleSubmit,
    formState: { errors },
    setValue,
    control,
  } = useForm<Category>();


  useEffect(() => {
    if (isEditing) {
      requestBackend({ url: `/categories/${categoryId}` }).then((response) => {
        const category = response.data as Category;

        setValue('name', category.name);
      });
    }
  }, [isEditing, categoryId, setValue]);

  const onSubmit = (formData: Category) => {
    const data = {
      ...formData,
    };
    
    console.log('categoria: '+ formData.name);
    const config: AxiosRequestConfig = {
      method: isEditing ? 'PUT' : 'POST',
      url: isEditing ? `/categories/${categoryId}` : '/categories',
      data,
      withCredentials: true,
    };

    requestBackend(config)
      .then(() => {
        toast.info('Categoria cadastrada com sucesso');
        history.push('/admin/categories');
      })
      .catch(() => {
        toast.error('Erro ao cadastrar categoria');
      });
  };

  const handleCancel = () => {
    history.push('/admin/categories');
  };

  return (
    <div className="category-crud-container">
      <div className="base-card category-crud-form-card">
        <h1 className="category-crud-form-title">DADOS DA CATEGORIA</h1>

        <form onSubmit={handleSubmit(onSubmit)}>
          <div className="row category-crud-inputs-container">
            <div className="col-lg-6 category-crud-inputs-left-container">
              <div className="margin-bottom-30">
                <input
                  {...register('name', {
                    required: 'Campo obrigatório',
                  })}
                  type="text"
                  className={`form-control base-input ${
                    errors.name ? 'is-invalid' : ''
                  }`}
                  placeholder="Nome da categoria"
                  name="name"
                />
                <div className="invalid-feedback d-block">
                  {errors.name?.message}
                </div>
              </div>
            </div>
            <div className="category-crud-buttons-container">
              <button
                className="btn btn-outline-danger category-crud-button"
                onClick={handleCancel}
              >
                CANCELAR
              </button>
              <button className="btn btn-primary category-crud-button text-white">
                SALVAR
              </button>
            </div>
          </div>
        </form>
      </div>
    </div>
  );
};

export default Form;
