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

  const url = 'http://localhost:8080/v1/api/developers/searchDeveloper'


  useEffect(() => {
    const storedDevelopers = JSON.parse(localStorage.getItem('allDevelopers')) || [];
    setAllDevelopers(storedDevelopers);

    const { developerId } = getQueryParams(); 
    setCheckedDeveloperId(developerId ? developerId : null); 
  }, [location.search]);



  const getQueryParams = () => {
    const urlParams = new URLSearchParams(location.search);

    return {
      gameName: urlParams.get('gameName') || '',
      genres: urlParams.get('genres') ? urlParams.get('genres').split(',') : [],
      platforms: urlParams.get('platforms') ? urlParams.get('platforms').split(',') : [],
      developerId: urlParams.get('developerId') || ''
    }
  }



  const handleChange = (e) => {
    // const { developerId } = getQueryParams();
    setDeveloperName(e.target.value)
    fetch(`${url}?developerName=${developerName}`)
      .then(response => {
        if (response.status === 200) {
          return response.json();
        } else {
          return Promise.reject(`Unexpected Status Code ${response.status}`)
        }
      })
      .then(data => {
        setDevelopers(data);
        data.forEach(newDeveloper => {
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

  const clearSelection = () => {
    setCheckedDeveloperId(null);

    const queryParams = new URLSearchParams(location.search);
    queryParams.delete("developerId");

    navigate(`?${queryParams.toString()}`, { replace: true });
  };

  const handleCheckboxChange = (developerId) => {
    const newDeveloperId = checkedDeveloperId === String(developerId) ? null : String(developerId);

    
    setCheckedDeveloperId(newDeveloperId);

    
    const queryParams = new URLSearchParams(location.search);
    if (newDeveloperId) {
      queryParams.set("developerId", newDeveloperId);
    } else {
      queryParams.delete("developerId");
    }

    navigate(`?${queryParams.toString()}`, { replace: true });
  };
  console.log(allDevelopers)
  const selectedDeveloper = allDevelopers.find(dev => String(dev.developerId) === String(checkedDeveloperId));
  console.log(selectedDeveloper)
  return (
    <>
      {selectedDeveloper && (
        <DeveloperButton
          developerId={checkedDeveloperId}
          developerName={selectedDeveloper.developerName}
          clearSelection={clearSelection}
        />
      )}

      <div>
        <Form.Control
          type="search"
          value={developerName}
          onChange={(e) => handleChange(e)}
          placeholder="Search Developer"
          aria-label="Search"
        />


        {/* Render list of checkboxes */}

        {developers.map((developer) => (
          <div key={developer.developerId}>
            <input
              type="checkbox"
              id={developer.developerId}
              checked={String(checkedDeveloperId) === String(developer.developerId)}
              onChange={() => handleCheckboxChange(developer.developerId)}
            />
            <label>{developer.developerName}</label>
          </div>
        ))}


      </div>
    </>)
}

export default DeveloperFilter;