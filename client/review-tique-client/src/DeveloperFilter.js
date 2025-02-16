import { useState } from "react";
import { useLocation } from "react-router-dom";
import { Form, Button } from "react-bootstrap";
function DeveloperFilter() {
    const [developers, setDevelopers] = useState([]);
    const [developerName, setDeveloperName] = useState('');
    const [developerId, setDeveloperId] = useState(null);
    const [checkedDeveloperIds, setCheckedDeveloperIds] = useState([]);
    const location = useLocation();
    const url = 'http://localhost:8080/v1/api/developers/searchDeveloper'
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
        .then(response =>{
            if(response.status === 200) {
                return response.json();
            }else{
                return Promise.reject(`Unexpected Status Code ${response.status}`)
            }
        })
        .then(data => setDevelopers(data))
        .catch(console.log)
    }

    const handleCheckboxChange = (developerId) => {
        setCheckedDeveloperIds((prevChecked) => {
          if (prevChecked.includes(developerId)) {
            return prevChecked.filter(id => id !== developerId); // Uncheck if already checked
          } else {
            return [...prevChecked, developerId]; // Check if not already checked
          }
        });
      };

    return (
        <>
    <section>
      <Form.Control
        type="search"
        value={developerName}
        onChange={(e) => handleChange(e)}
        placeholder="Search Developer"
        aria-label="Search"
      />
      
      
      {/* Render list of checkboxes */}
      <div>
        {developers.length > 0 ? (
          developers.map(developer => (
            <div key={developer.developerId}>
              <input
                type="checkbox"
                id={`developer-${developer.developerId}`}
                checked={checkedDeveloperIds.includes(developer.developerId)}
                onChange={() => handleCheckboxChange(developer.developerId)}
              />
              <label htmlFor={`developer-${developer.developerId}`}>
                {developer.developerName}
              </label>
            </div>
          ))
        ) : (
          <></>
        )}
      </div>
    </section>
  </>)
}

export default DeveloperFilter;