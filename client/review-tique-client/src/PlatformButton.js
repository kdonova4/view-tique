import { Button } from "react-bootstrap";
import { FaTimesCircle } from "react-icons/fa";
function PlatformButton({ platform, clearSelection }) {
    return(<>
        <div className="mt-4 mb-4">
            <Button variant="info" onClick={() => clearSelection(platform.platformId)} className="platform-button">
                <FaTimesCircle className="filter-icon"/> {platform.platformName}
            </Button>
        </div>
    </>)
}

export default PlatformButton;