import { Link } from "react-router-dom";
import GameSearch from "./GameSearch";

function NavBar(){
    return(
        <nav>
            <Link to={'/'}>Home</Link>
            <GameSearch/>
            <Link to={'/search'}>Search</Link>
            <Link to={'/game/add'}>Profile</Link>
        </nav>
    )
}

export default NavBar;