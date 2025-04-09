
import React, { useState } from "react";
import { Form, Button } from "react-bootstrap";
import { useLocation, useNavigate } from "react-router-dom";
function GameSearch() {
    const [gameName, setGameName] = useState('');
    
    const navigate = useNavigate();
    const location = useLocation();
    const handleSubmit = (event) => {
        event.preventDefault();
        const queryParams = new URLSearchParams(location.search);
        queryParams.set("gameName", gameName);
        navigate(`games/search?${queryParams.toString()}`, { replace: true });
        
    }

    return(<>
        <div>
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

