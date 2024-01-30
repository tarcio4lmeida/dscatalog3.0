import ProductPrice from 'components/ProductPrice';
import './styles.css';
import ProdutctImg from 'assets/images/product.png';

const ProductCard = () => {
  return (
    <div className="base-card product-card">
      <div className="card-top-container">
        <img src={ProdutctImg} alt="Nome do produto" />
      </div>
      <div className="card-bottom-container">
        <h6>Nome do produto</h6>
        <ProductPrice/>
      </div>
    </div>
  );
};

export default ProductCard;
