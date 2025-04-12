import { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import GenreButton from "./GenreButton";
import { Button, Form } from "react-bootstrap";

function GenreFilter() {
    const [genres, setGenres] = useState([]);
    const [allGenres, setAllGenres] = useState([]);
    const [genreName, setGenreName] = useState('');
    const [genreIds, setGenreIds] = useState([]);
    const [checkedGenreIds, setCheckedGenreIds] = useState([]);

    const location = useLocation();
    const navigate = useNavigate();

    const url = 'http://localhost:8080/api/v1/genres/searchGenre'
    useEffect(() => {
        const storedGenres = JSON.parse(localStorage.getItem('allGenres')) || [];
        setAllGenres(storedGenres);

        const { genres } = getQueryParams();
        setCheckedGenreIds(genres.length > 0 ? genres : []);
    }, [location.search]);

    const getQueryParams = () => {
        const urlParams = new URLSearchParams(location.search); // get params from url

        return {
            gameName: urlParams.get('gameName') || '',
            genres: urlParams.get('genres') ? urlParams.get('genres').split(',') : [],
            platforms: urlParams.get('platforms') ? urlParams.get('platforms').split(',') : [],
            developerId: urlParams.get('developerId') || ''
        }
    }

    const clearSelection = (genreId) => {
        const newGenreIds = checkedGenreIds.filter(genre => genre !== String(genreId))

        setCheckedGenreIds(newGenreIds);

        const queryParams = new URLSearchParams(location.search);

        if (newGenreIds.length > 0) {
            queryParams.set('genres', newGenreIds.join(','))
        } else {
            queryParams.delete('genres')
        }

        navigate(`?${queryParams.toString()}`, { replace: true });
    }

    const handleChange = (e) => {
        const value = e.target.value
        setGenreName(value);
        fetch(`${url}?genreName=${value}`)
            .then(response => {
                if (response.status === 200) {
                    return response.json();
                } else {
                    return Promise.reject(`Unexpected Status Code ${response.status}`);
                }
            })
            .then(data => {
                setGenres(data);
                setAllGenres((prevGenres) => {
                    const newGenres = data.filter(newGenre =>
                        !prevGenres.some(genre => genre.genreId === newGenre.genreId)
                    );

                    if (newGenres.length === 0) return prevGenres;

                    const updatedGenres = [...prevGenres, ...newGenres];

                    localStorage.setItem('allGenres', JSON.stringify(updatedGenres))

                    return updatedGenres;
                })
            })
    }

    const handleCheckboxChange = (genreId) => {
        // if genreId is in the list then remove it, otherwise add it to the list
        const newGenreIds = checkedGenreIds.includes(String(genreId))
            ? checkedGenreIds.filter((id) => id !== String(genreId))
            : [...checkedGenreIds, String(genreId)];

        setCheckedGenreIds(newGenreIds);
        console.log(checkedGenreIds)

        const queryParams = new URLSearchParams(location.search);
        if (newGenreIds.length > 0) {
            queryParams.set("genres", newGenreIds.join(','));
        } else {
            queryParams.delete("genres");
        }

        navigate(`?${queryParams.toString()}`, { replace: true });
    }

    const selectedGenres = allGenres.filter((genre) =>
        checkedGenreIds.includes(String(genre.genreId)));

    return (<>
        <div className="filters m-4">
            {selectedGenres.length > 0 && (
                selectedGenres.map((genre) => (
                    <GenreButton
                        key={genre.genreId}
                        genre={genre}
                        clearSelection={() => clearSelection(genre.genreId)}
                    />
                ))

            )}


            <div>
                <Form.Control
                    className="genre"
                    type="search"
                    value={genreName}
                    onChange={(e) => handleChange(e)}
                    placeholder="Search Genre"
                    aria-label="Search"
                />


                {genres.map((genre) => (
                    <Button
                        variant="primary"
                        className="genre-style ml-1"
                        key={genre.genreId}
                        onClick={() => handleCheckboxChange(genre.genreId)}
                        style={{ cursor: 'pointer' }}
                    >
                        <input
                            className="ml-3"
                            type="checkbox"
                            id={genre.genreId}
                            onChange={() => { } /* Prevent direct checkbox toggling */}
                            checked={checkedGenreIds.includes(String(genre.genreId))}
                            hidden
                        />
                        <label className="" style={{ cursor: 'pointer' }}>
                            {genre.genreName}
                        </label>
                    </Button>
                ))}
            </div>
        </div>
    </>)
}

export default GenreFilter;