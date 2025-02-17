import { Button } from "react-bootstrap";

function DeveloperButton({ developerId, developerName, clearSelection }) {
    return(<>
        <div className="developer-button mt-4 mb-4">
            <Button variant="secondary" onClick={clearSelection}>X {developerName}</Button>
        </div>
    </>)
}

export default DeveloperButton;