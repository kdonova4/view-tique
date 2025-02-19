import { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { Form, Button } from "react-bootstrap";
import PlatformButton from "./PlatformButton";
function PlatformFilter() {
    const [platforms, setPlatforms] = useState([]);
    const [allPlatforms, setAllPlatforms] = useState([]);
    const [platformName, setPlatformName] = useState('');
    const [platformIds, setPlatformsId] = useState([]);
    const [checkedPlatformIds, setCheckedPlatformIds] = useState([]);

    const location = useLocation();
    const navigate = useNavigate();

    const url = 'http://localhost:8080/v1/api/platforms/searchPlatform'

    useEffect(() => {
        const storedPlatforms = JSON.parse(localStorage.getItem('allPlatforms')) || [];
        setAllPlatforms(storedPlatforms);

        const { platforms } = getQueryParams();
        setCheckedPlatformIds(platforms.length > 0 ? platforms : []);
    },[location.search]);

    const getQueryParams = () => {
        const urlParams = new URLSearchParams(location.search); // get params from url

        return {
            gameName: urlParams.get('gameName') || '',
            genres: urlParams.get('genres') ? urlParams.get('genres').split(',') : [],
            platforms: urlParams.get('platforms') ? urlParams.get('platforms').split(',') : [],
            developerId: urlParams.get('developerId') || ''
        }
    }

    const clearSelection = (platformId) => {
        const newPlatformsIds =  checkedPlatformIds.filter(plat => plat !== String(platformId))

        setCheckedPlatformIds(newPlatformsIds);

        const queryParams = new URLSearchParams(location.search);
        
        if(newPlatformsIds.length > 0) {
            queryParams.set('platforms', newPlatformsIds.join(','))
        }else {
            queryParams.delete('platforms')
        }

        navigate(`?${queryParams.toString()}`, { replace: true });
    }
    
    const handleChange = (e) => {
        const value = e.target.value;
        setPlatformName(value);
        
        fetch(`${url}?platformName=${value}`)
        .then(response => {
            if(response.status === 200) {
                return response.json();
            }else {
                return Promise.reject(`Unexpected Status Code ${response.status}`)
            }
        })
        .then(data => {
        
            setPlatforms(data);
            setAllPlatforms((prevPlatforms) => {
                const newPlatforms = data.filter(newPlatform =>
                    !prevPlatforms.some(plat => plat.platformId === newPlatform.platformId)
                );

                if(newPlatforms.length === 0) return prevPlatforms;

                const updatedPlatforms = [...prevPlatforms, ...newPlatforms];

                localStorage.setItem('allPlatforms', JSON.stringify(updatedPlatforms))

                return updatedPlatforms;
            })
        })
        .catch(console.log);
    }

    const handleCheckboxChange = (platformId) => {
        // if platformId is in the list then remove it, otherwise add it to the list
        const newPlatformIds = checkedPlatformIds.includes(String(platformId))
            ? checkedPlatformIds.filter((id) => id !== String(platformId))
            : [...checkedPlatformIds, String(platformId)];

        setCheckedPlatformIds(newPlatformIds);
        console.log(checkedPlatformIds)

        const queryParams = new URLSearchParams(location.search);
        if (newPlatformIds.length > 0) {
            queryParams.set("platforms", newPlatformIds.join(','));
        } else {
            queryParams.delete("platforms");
        }

        navigate(`?${queryParams.toString()}`, { replace: true });
    }

    const selectedPlatforms = allPlatforms.filter((platform) =>
    checkedPlatformIds.includes(String(platform.platformId)));
    
    return (<>

        {selectedPlatforms.length > 0 && (
            selectedPlatforms.map((platform) => (
                <PlatformButton
                key={platform.platformId}
                platform={platform}
                clearSelection={() => clearSelection(platform.platformId)}
            />
            ))
            
        )}

        
        <div>
            <Form.Control
                type="search"
                value={platformName}
                onChange={(e) => handleChange(e)}
                placeholder="Search Platform"
                aria-label="Search"
            />


            {platforms.map((platform) => (
                <div key={platform.platformId}>
                    <input
                        type="checkbox"
                        id={platform.platformId}
                        onChange={() => {
                            console.log(checkedPlatformIds.includes(String(platform.platformId)));
                            handleCheckboxChange(platform.platformId)}   
                        }
                        checked={checkedPlatformIds.includes(String(platform.platformId))}
                        
                        
                    />
                    <label>{platform.platformName}</label>
                </div>
            ))}
        </div>
    </>)
}

export default PlatformFilter;