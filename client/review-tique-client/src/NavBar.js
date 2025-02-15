import { Link } from "react-router-dom";

function NavBar(){
    return(
        <nav>
            <Link to={'/'}>Home</Link>
            <Link to={'/games'}>See All Games</Link>
            <Link to={'/game/add'}>Add a Game</Link>
        </nav>
    )
}

export default NavBar;