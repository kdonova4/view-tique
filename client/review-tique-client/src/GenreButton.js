import { Button } from "react-bootstrap";
import { FaTimesCircle } from "react-icons/fa";
function GenreButton({ genre, clearSelection }) {
    return(<>
        <div className="mt-4 mb-4">
            <Button variant="info" onClick={() => clearSelection(genre.genreId)} className="genre-button">
                <FaTimesCircle className="filter-icon"/> {genre.genreName}
            </Button>
        </div>
    </>)
}

export default GenreButton;