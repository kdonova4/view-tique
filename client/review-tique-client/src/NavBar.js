import { Link } from "react-router-dom";
import GameSearch from "./GameSearch";
import { useAuth } from "./context/AuthContext";
import Nav from "react-bootstrap/Nav";
import Navbar from "react-bootstrap/Navbar";
import { Container } from "react-bootstrap";
function NavBar(){
    const { token, logout, role } = useAuth();
    return(
        <Navbar>
            <Container>
            <Nav.Link as={Link} to={"/"}>Home</Nav.Link>
            <GameSearch/>
            {token ? (
                <>
                    <Nav.Link as={Link} to={"/"}>Profile</Nav.Link>
                    <Nav.Link onClick={logout}>Logout</Nav.Link>
                </>
                
            ) : (
                <>
                <Nav.Link as={Link} to={"/login"}>
                  Log In
                </Nav.Link>
                <Nav.Link as={Link} to={"/register"}>
                  Register
                </Nav.Link>
              </>
            )}
            
            </Container>
        </Navbar>
    )
}

export default NavBar;