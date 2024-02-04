import './styles.css';
import { ReactComponent as ArrowIcon } from 'assets/images/arrow.svg';
import { NavLink } from 'react-router-dom';

type Props = {
  text: string;
};

const ButtonIcon = ({ text }: Props) => {
  return (
    <div className="btn-container">
      <div className="btn-container button">
        <button className="btn btn-primary">
          <h6>{text}</h6>
        </button>
      </div>
      <NavLink to="/products">
        <div className="btn-icon-container">
          <ArrowIcon />
        </div>
      </NavLink>
    </div>
  );
};

export default ButtonIcon;
