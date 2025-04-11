import GameList from "./GameList";
import { FaRegCopyright } from "react-icons/fa";
function Home() {
    return (<>
        <div className="container home">

            <div>
                <img
                    src="/review-tique-logo-480x480.png"
                    alt="Brand Logo"
                    height="640"
                    width="640"
                    className="d-inline-block align-top"
                />
            </div>

            <div className="home-text">
                <h1 style={{ borderBottom: `1px solid gray` }}>What is This Project?</h1>
                <p>This Game Review Web Application allows users to post reviews as either a critic or a regular user. Each game displays two separate average scores—one from critics and one from users—to keep both perspectives distinct. Users can edit or delete their own reviews, as well as add games to a wishlist for future reference. All personal reviews and wishlisted games are viewable on the user's profile page.</p>

                <p>To encourage engagement while maintaining structure, only registered users can interact with the platform. Unauthenticated visitors can freely browse and search the game catalog, but cannot leave reviews, like/dislike content, or add games to wishlists. Those features are reserved for logged-in users and critics.</p>

                <p>The game data powering this app was sourced from the IGDB API and imported into my own MySQL database. While the data is static in the current version, if I revisit this project in the future, I plan to implement web sockets or scheduled updates to keep the database in sync with the IGDB API.</p>

                <p>The reason I opted to build and manage my own database—rather than rely directly on the API—was to deepen my understanding of full-stack development, particularly around data modeling, transformation, and persistence. This project was both a technical challenge and a valuable learning experience.</p>

                <p>The reason I opted to build and manage my own database—rather than rely directly on the API—was to deepen my understanding of full-stack development, particularly around data modeling, transformation, and persistence. This project was both a technical challenge and a valuable learning experience.</p>


                <h2 className="mt-5" style={{ borderBottom: `1px solid gray` }}>Technologies Used</h2>
                <div className="tech-grid">
                    <div className="technologies">
                        <div className="tech">
                            <img className="tech-image" src="https://www.devopsschool.com/blog/wp-content/uploads/2023/12/image-168.png" alt="tech Logo" />
                            <div className="tech-title text-muted">
                                <span>Spring Boot</span>
                            </div>
                        </div>
                        <div className="tech">
                            <img style={{ borderRadius: '10px' }} className="tech-image" src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRjzySo0vHp2daaRLtnMnHLpMXplfFj73Dxmg&s" alt="tech Logo" />
                            <div className="tech-title text-muted">
                                <span>MySQL</span>
                            </div>
                        </div>
                        <div className="tech">
                            <img className="tech-image" src="https://static-00.iconduck.com/assets.00/java-icon-378x512-w60vlu77.png" alt="tech Logo" />
                            <div className="tech-title  text-muted">
                                <span>Java</span>
                            </div>
                        </div>
                        <div className="tech">
                            <img style={{ borderRadius: '10px' }} className="tech-image" src="https://2ality.com/2011/10/logo-js/js.jpg" alt="tech Logo" />
                            <div className="tech-title  text-muted">
                                <span>Javascript</span>
                            </div>
                        </div>
                        <div className="tech">
                            <img style={{ borderRadius: '10px' }} className="tech-image" src="https://upload.wikimedia.org/wikipedia/commons/thumb/a/a7/React-icon.svg/1200px-React-icon.svg.png" alt="tech Logo" />
                            <div className="tech-title text-muted">
                                <span>React</span>
                            </div>
                        </div>
                        <div className="tech">
                            <img className="tech-image" src="https://archive.org/download/github.com-swagger-api-swagger-ui_-_2021-09-30_17-43-44/cover.jpg" alt="tech Logo" />
                            <div className="tech-title text-muted">
                                <span>Swagger</span>
                            </div>
                        </div>
                        <div className="tech">
                            <img className="tech-image" src="https://junit.org/junit5/assets/img/junit5-logo.png" alt="tech Logo" />
                            <div className="tech-title text-muted">
                                <span>JUnit</span>
                            </div>
                        </div>
                        <div className="tech">
                            <img className="tech-image" src="https://raw.githubusercontent.com/mockito/mockito/main/config/javadoc/resources/org/mockito/logo.png" alt="tech Logo" />
                            <div className="tech-title text-muted">
                                <span>Mockito</span>
                            </div>
                        </div>
                        <div className="tech">
                            <img className="tech-image" src="https://dl.flathub.org/media/com/jetbrains/IntelliJ-IDEA-Community/1e0c1aebda218f43e33d2b676f888038/icons/128x128@2/com.jetbrains.IntelliJ-IDEA-Community.png" alt="tech Logo" />
                            <div className="tech-title text-muted">
                                <span>IntelliJ IDEA</span>
                            </div>
                        </div>
                        <div className="tech">
                            <img style={{ borderRadius: '10px' }} className="tech-image" src="https://upload.wikimedia.org/wikipedia/commons/thumb/9/9a/Visual_Studio_Code_1.35_icon.svg/2048px-Visual_Studio_Code_1.35_icon.svg.png" alt="tech Logo" />
                            <div className="tech-title text-muted">
                                <span>VS Code</span>
                            </div>
                        </div>
                        <div className="tech">
                            <img className="tech-image" src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT-TB9d5YXwtKhv4NWbpeTBVveYvcxu9gMJng&s" alt="tech Logo" />
                            <div className="tech-title text-muted">
                                <span>Postman</span>
                            </div>
                        </div>
                        <div className="tech">
                            <img className="tech-image" src="https://images.seeklogo.com/logo-png/27/2/node-js-logo-png_seeklogo-273750.png" alt="tech Logo" />
                            <div className="tech-title text-muted">
                                <span>NodeJS</span>
                            </div>
                        </div>
                        <div className="tech">
                            <img className="tech-image" src="https://topsalaries.tech/assets/logos/docker.jpg" alt="tech Logo" />
                            <div className="tech-title text-muted">
                                <span>Docker</span>
                            </div>
                        </div>
                    </div>


                </div>
                <h2 className="mt-5">Made By Kevin Donovan</h2>
            </div>


        </div>

    </>)
}

export default Home;