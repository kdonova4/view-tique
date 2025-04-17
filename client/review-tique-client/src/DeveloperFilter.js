import { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { Form, Button } from "react-bootstrap";
import DeveloperButton from "./DeveloperButton";
function DeveloperFilter() {
  const [developers, setDevelopers] = useState([]);
  const [allDevelopers, setAllDevelopers] = useState([]);
  const [developerName, setDeveloperName] = useState('');
  const [developerId, setDeveloperId] = useState(null);
  const [checkedDeveloperId, setCheckedDeveloperId] = useState(null);

  const location = useLocation();
  const navigate = useNavigate();

  const url = 'http://localhost:8080/api/v1/developers/searchDeveloper'


  useEffect(() => {
    const storedDevelopers = JSON.parse(localStorage.getItem('allDevelopers')) || [];
    setAllDevelopers(storedDevelopers); // get stored developers for the devloper filter button

    const { developerId } = getQueryParams(); 
    setCheckedDeveloperId(developerId ? developerId : null); // if there is a devloperId set checkedDevloper
  }, [location.search]); // Re-render when the url changes



  const getQueryParams = () => {
    const urlParams = new URLSearchParams(location.search); // get params from url

    return {
      gameName: urlParams.get('gameName') || '',
      genres: urlParams.get('genres') ? urlParams.get('genres').split(',') : [],
      platforms: urlParams.get('platforms') ? urlParams.get('platforms').split(',') : [],
      developerId: urlParams.get('developerId') || ''
    }
  }



  const handleChange = (e) => {
    // set devloper name used for searching to the value in the search bar
    const value = e.target.value;
    setDeveloperName(value);
    if(value === "") {
      setDevelopers([]);
    } else { fetch(`${url}?developerName=${developerName}`)// fetch list of developers using the devloperName and searchByName method in Backend
      .then(response => {
        if (response.status === 200) {
          return response.json();
        } else {
          return Promise.reject(`Unexpected Status Code ${response.status}`)
        }
      })
      .then(data => {
        setDevelopers(data); // set current developers
        data.forEach(newDeveloper => { // adding developers to persistent array for the developer button
          setAllDevelopers((prevDevelopers) => {
            const isDeveloperExists = prevDevelopers.some(dev => dev.developerId === newDeveloper.developerId);
            if (!isDeveloperExists) {
              const updatedDevelopers = [...prevDevelopers, newDeveloper];
              localStorage.setItem('allDevelopers', JSON.stringify(updatedDevelopers));
              return updatedDevelopers; 
            }
            return prevDevelopers; 
          });
        });
      })
      .catch(console.log)
    }
  }

  const clearSelection = () => {
    setCheckedDeveloperId(null);
    // remove devloperId from url when removed filter
    const queryParams = new URLSearchParams(location.search);
    queryParams.delete("developerId");
    
    navigate(`?${queryParams.toString()}`, { replace: true });
  };

  const handleCheckboxChange = (developerId) => {
    // if developersId is equal to checkedDeveloper than uncheck, else make checkedDev the new Id
    const newDeveloperId = checkedDeveloperId === String(developerId) ? null : String(developerId);
    setCheckedDeveloperId(newDeveloperId);

    // update url
    const queryParams = new URLSearchParams(location.search);
    if (newDeveloperId) {
      queryParams.set("developerId", newDeveloperId);
    } else {
      queryParams.delete("developerId");
    }

    navigate(`?${queryParams.toString()}`, { replace: true });
  };

  // get the selected developer before rendering using persistent list
  const selectedDeveloper = allDevelopers.find(dev => String(dev.developerId) === String(checkedDeveloperId));

  return (
    <>
    <div className="filters m-4">
      {selectedDeveloper && (
        <DeveloperButton
          developerId={checkedDeveloperId}
          developerName={selectedDeveloper.developerName}
          clearSelection={clearSelection}
        />
      )}
    
      <div>
        <Form.Control

          className="dev"
          type="search"
          value={developerName}
          onChange={(e) => handleChange(e)}
          placeholder="Search Developer"
          aria-label="Search"
        />


        

        {developers.map((developer) => (
          <Button
                        variant="primary"
                        className="developer-style ml-1"
                        key={developer.de}
                        onClick={() => handleCheckboxChange(developer.developerId)}
                        style={{ cursor: 'pointer' }}
                    >
            <input
              className="ml-3"
              type="checkbox"
              id={developer.developerId}
              checked={(String(checkedDeveloperId) === String(developer.developerId))}
              onChange={() => { }}
              hidden
            />
            <label className="" style={{ cursor: 'pointer' }}>
                            {developer.developerName}
                        </label>
          </Button>
        ))}


      </div>
      </div>
    </>)
}

export default DeveloperFilter;