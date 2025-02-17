import { Button } from "react-bootstrap";
import { FaTimesCircle } from "react-icons/fa";
function DeveloperButton({ developerId, developerName, clearSelection }) {
    return(<>
        <div className=" mt-4 mb-4">
            <Button variant="info" onClick={clearSelection} className="developer-button">
                <FaTimesCircle className="filter-icon"/> {developerName}
            </Button>
        </div>
    </>)
}

export default DeveloperButton;