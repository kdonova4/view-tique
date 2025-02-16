
import React, { useState } from "react";
import { Form, Button } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
function GameSearch() {
    const [gameName, setGameName] = useState('');
    
    const navigate = useNavigate();

    const handleSubmit = (event) => {
        event.preventDefault();
        if(gameName) {
            navigate(`/games/search?gameName=${gameName}`)
        }else {
            navigate(`/games/search`)
        }
        
    }

    return(<>
        <h1>Search Bar</h1>
        <div className="container">
            <Form className="d-flex" onSubmit={handleSubmit}>
            <Form.Control
              type="search" 
              value={gameName}
              placeholder="Search"
              className="me-2"
              onChange={(e) => setGameName(e.target.value)}
              aria-label="Search"
            />
        <Button className="ml-4" variant="outline-success" type="submit">Search</Button>
        </Form>
        </div>
    </>)
}

export default GameSearch;

