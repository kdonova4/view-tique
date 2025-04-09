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
              
            <Nav.Link as={Link} to={"/"} style={{ width: `120px`, minWidth: `120px` }}>
            <img
            src="/review-tique-logo-480x480.png"
            alt="Brand Logo"
            height="80"
            className="d-inline-block align-top"
          />
            </Nav.Link>
            <div className="game-search">
              <GameSearch/>
            </div>
            
            {token ? (
                <div className="profile">
                    <Nav.Link as={Link} to={"/profile"}>Profile</Nav.Link>
                    <Nav.Link onClick={logout}>Logout</Nav.Link>
                </div>
                
            ) : (
                <div className="profile">
                <Nav.Link as={Link} to={"/login"} style={{ width: `70px` }}>
                  Log In
                </Nav.Link>
                <Nav.Link as={Link} to={"/register"}>
                  Register
                </Nav.Link>
              </div>
            )}
            
            </Container>
        </Navbar>
    )
}

export default NavBar;