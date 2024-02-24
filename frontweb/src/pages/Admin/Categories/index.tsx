import { Route, Switch } from "react-router-dom";
import List from "./List";
import Form from "./Form";



const Categories = () => {

    return (
        <Switch>
            <Route path="/admin/categories" exact>
                <List />
            </Route>
            <Route path="/admin/categories/:categoryId">
                <Form />
            </Route>
        </Switch>
    )
}

export default Categories;
