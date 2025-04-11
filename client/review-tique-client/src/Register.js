import { useState } from "react";
import { useNavigate } from "react-router-dom";

function Register() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");

    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const handleSubmit = async (e, role) => {
        e.preventDefault();

        setLoading(true);
        setError("");

        const payload = { username, password };
        console.log(role)
        try {
          let response = null;
          if(role === 'user') {
            response = await fetch (
              "http://localhost:8080/v1/api/user/register/user",
              {
                  method: 'POST',
                  headers: {
                      "Content-Type": "application/json"
                  },
                  body: JSON.stringify(payload)
              }
          );
          } else if(role === 'critic') {
            response = await fetch (
              "http://localhost:8080/v1/api/user/register/critic",
              {
                  method: 'POST',
                  headers: {
                      "Content-Type": "application/json"
                  },
                  body: JSON.stringify(payload)
              }
          );
          }
            

            if(response.ok) {
                navigate("/login");
            }else {
                const errorMessage = await response.text();
                setError(errorMessage || "Registration Failed, Try Again")
            } 
            
        }catch(err) {
            setError("Registration Failed, Try Again")
            console.error("Registration Error: ", err);
        } finally {
            setLoading(false);
        }
        
    }


    return (
        <div className="container d-flex justify-content-center align-items-center min-vh-100">
          <div className="col-12 col-md-6 col-lg-4">
            <h2 className="text-center mb-4">Register</h2>
            <form>
              <div className="mb-3">
                <label htmlFor="username" className="form-label">
                  Username
                </label>
                <input
                  type="text"
                  id="username"
                  className="form-control"
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                  required
                />
              </div>
    
              <div className="mb-3">
                <label htmlFor="password" className="form-label">
                  Password
                </label>
                <input
                  type="password"
                  id="password"
                  className="form-control"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  required
                />
              </div>
    
              {error && <div className="alert alert-danger">{error}</div>}
    
    
              <button
                onClick={(event) => handleSubmit(event, 'user')}
                className="btn btn-primary w-100"
                disabled={loading}
              >
                {loading ? "Registering..." : "Register as User"}
              </button>
              <button
                
                onClick={(event) => handleSubmit(event, 'critic')}
                
                className="btn btn-primary w-100 mt-4"
                disabled={loading}
              >
                {loading ? "Registering..." : "Register as Critic"}
              </button>
            </form>
          </div>
    
        </div>
      );
}

export default Register;