Quick Start
===========

1. Run `mvn clean package` from the base directory of this project.
2. Move the .war file in the **/target** folder to your Tomcat **/webapps** directory and rename it to **treasury.war**.
3. Copy the **treasury.properties.example** file into your Tomcat **/lib** directory and rename it to **treasury.properties**.
4. Review **treasury.properties** and make any necessary modifications.
5. Place libraries containing the JDBC drivers configured in **treasury.properties** into your Tomcat **/lib** directory.
6. Launch Tomcat.

Next Steps
==========

1. Make sure that your treasury is accessible via the domain name you configured in **treasury.properties**.
2. You should host your treasury exclusively via HTTPS on port 443 using a professionally-signed certificate.
3. You should throttle requests to your treasury to help prevent brute force and [DDoS] attacks.
4. You should configure a more reliable database connection than the default one.
5. You should run multiple instances of your treasury and feed all requests through a load balancer.


[DDOS]: http://en.wikipedia.org/wiki/Denial-of-service_attack
