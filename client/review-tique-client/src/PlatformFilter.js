import { useState } from "react";
import { useLocation } from "react-router-dom";

function PlatformFilter() {
    const [platforms, setPlatforms] = useState([])
    const [platformName, setPlatformName] = useState('');
    const [platformIds, setPlatformsId] = useState([]);
    const [checkedPlatformIds, setCheckedPlatformIds] = useState([]);
    
    const location = useLocation
    const url = 'http://localhost:8080/v1/api/platforms/searchPlatform'
}

export default PlatformFilter;