<template>
  <div class="home-container">
    <!-- Hero Section -->
    <section class="hero">
      <div class="hero-content">
        <h1>Build Your Business, Secure Your Future</h1>
        <p>Professional Financial Services based in London, serving clients worldwide with expert solutions</p>
        <div class="hero-contact">
          <router-link to="/contact" class="cta-button">CONTACT US</router-link>
        </div>
      </div>
    </section>

    <!-- Services Section -->
    <section class="services-section">
      <div class="section-content">
        <h2>Our Services</h2>
        <p class="section-description">
          With our professional expertise and industry accreditations, we provide comprehensive financial solutions tailored to your needs.
        </p>
        <div class="services-grid">
          <div class="service-card">
            <i class="fas fa-chart-line"></i>
            <h3>Investment Management</h3>
            <p>Strategic investment solutions for individuals and businesses</p>
          </div>
          <div class="service-card">
            <i class="fas fa-coins"></i>
            <h3>Wealth Planning</h3>
            <p>Comprehensive wealth management and growth strategies</p>
          </div>
          <div class="service-card">
            <i class="fas fa-building"></i>
            <h3>Corporate Finance</h3>
            <p>Expert financial solutions for businesses of all sizes</p>
          </div>
        </div>
        <router-link to="/services" class="secondary-button">FIND OUT MORE</router-link>
      </div>
    </section>

    <!-- Contact Section -->


    <!-- Why Choose Us Section -->
    <section class="why-us-section">
      <div class="section-content">
        <h2>Why Choose Us?</h2>
        <p class="section-description">
          We understand the complexities of modern finance and are committed to helping our clients achieve their financial goals.
        </p>
        <div class="features-grid">
          <div class="feature-card">
            <i class="fas fa-shield-alt"></i>
            <h3>Trusted Expertise</h3>
            <p>Over 25 years of experience delivering exceptional financial services with proven track record of success</p>
          </div>
          <div class="feature-card">
            <i class="fas fa-users"></i>
            <h3>Loads of experience</h3>
            <p>Over 25 years of experience delivering exceptional financial services with proven track record of success</p>
          </div>
          <div class="feature-card">
            <i class="fas fa-globe"></i>
            <h3>24/7 Global Support</h3>
            <p>Round-the-clock assistance and consultation services for clients across all major international markets</p>
          </div>
        </div>
      </div>
    </section>

    <!-- Process Section -->
    <section class="process-section">
      <div class="section-content">
        <h2>How We Work</h2>
        <p class="section-description">
          Our streamlined process ensures a smooth and efficient experience for all our clients
        </p>
        <div class="process-grid">
          <div class="process-card">
            <div class="process-number">1</div>
            <i class="fas fa-comments"></i>
            <h3>Request Quote</h3>
            <p>Share your requirements with us through our simple online form or direct contact</p>
          </div>
          <div class="process-card">
            <div class="process-number">2</div>
            <i class="fas fa-clock"></i>
            <h3>Initial Consultation</h3>
            <p>We'll reach out within 24 hours to schedule a detailed discussion about your needs</p>
          </div>
          <div class="process-card">
            <div class="process-number">3</div>
            <i class="fas fa-file-signature"></i>
            <h3>Custom Proposal</h3>
            <p>Receive a tailored service proposal including detailed scope and pricing</p>
          </div>
          <div class="process-card">
            <div class="process-number">4</div>
            <i class="fas fa-handshake"></i>
            <h3>Begin Partnership</h3>
            <p>Sign the agreement and start working together towards your financial goals</p>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>


<style scoped>
.home-container {
  width: 100%;
}

.hero {
  background: linear-gradient(rgba(0, 0, 0, 0.7), rgba(0, 0, 0, 0.7)),
              url('@/assets/building.png') center/cover;
  height: 80vh;
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
  color: white;
  padding: 0 20px;
}

.hero-content {
  max-width: 800px;
}

.hero-content h1 {
  font-size: 3.5rem;
  margin-bottom: 1.5rem;
}

.hero-content p {
  font-size: 1.2rem;
  margin-bottom: 2rem;
}

.hero-contact {
  display: flex;
  gap: 2rem;
  justify-content: center;
  align-items: center;
}

.phone {
  color: white;
  text-decoration: none;
  font-size: 1.2rem;
  font-weight: bold;
}

section {
  padding: 5rem 2rem;
}

.section-content {
  max-width: 1200px;
  margin: 0 auto;
  text-align: center;
}

h2 {
  font-size: 2.5rem;
  margin-bottom: 1.5rem;
  color: #333;
}

.section-description {
  font-size: 1.2rem;
  color: #666;
  max-width: 800px;
  margin: 0 auto 3rem;
}

.services-grid, .features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 2rem;
  margin-bottom: 3rem;
}

.service-card, .feature-card {
  padding: 2rem;
  background: white;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease;
}

.service-card:hover, .feature-card:hover {
  transform: translateY(-5px);
}

.service-card i, .feature-card i {
  font-size: 2.5rem;
  color: #007bff;
  margin-bottom: 1.5rem;
}

.service-card h3, .feature-card h3 {
  font-size: 1.5rem;
  margin-bottom: 1rem;
  color: #333;
}

.service-card p, .feature-card p {
  color: #666;
}

.cta-button {
  display: inline-block;
  padding: 1rem 2.5rem;
  background: #007bff;
  color: white;
  text-decoration: none;
  border-radius: 5px;
  font-weight: bold;
  transition: background 0.3s ease;
}

.cta-button:hover {
  background: #0056b3;
}

.secondary-button {
  display: inline-block;
  padding: 1rem 2.5rem;
  background: transparent;
  color: #007bff;
  text-decoration: none;
  border: 2px solid #007bff;
  border-radius: 5px;
  font-weight: bold;
  transition: all 0.3s ease;
}

.secondary-button:hover {
  background: #007bff;
  color: white;
}

.contact-section {
  background: #f8f9fa;
}

.contact-note {
  font-size: 1.1rem;
  color: #666;
  margin-bottom: 2rem;
}

.why-us-section {
  background: #f8f9fa;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 2rem;
  margin-bottom: 3rem;
}

.feature-card {
  padding: 2rem;
  background: white;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease;
}

.feature-card:hover {
  transform: translateY(-5px);
}

.feature-card i {
  font-size: 2.5rem;
  color: #007bff;
  margin-bottom: 1.5rem;
}

.feature-card h3 {
  font-size: 1.5rem;
  margin-bottom: 1rem;
  color: #333;
}

.feature-card p {
  color: #666;
}

.process-section {
  background: #f8f9fa;
}

.process-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 2rem;
  margin-bottom: 3rem;
}

.process-card {
  padding: 2rem;
  background: white;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease;
  position: relative;
}

.process-number {
  position: absolute;
  top: -15px;
  left: -15px;
  width: 40px;
  height: 40px;
  background: #007bff;
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  font-size: 1.2rem;
}

.process-card:hover {
  transform: translateY(-5px);
}

.process-card i {
  font-size: 2.5rem;
  color: #007bff;
  margin-bottom: 1.5rem;
}

.process-card h3 {
  font-size: 1.5rem;
  margin-bottom: 1rem;
  color: #333;
}

.process-card p {
  color: #666;
}

@media (max-width: 768px) {
  .hero-content h1 {
    font-size: 2.5rem;
  }

  .hero-contact {
    flex-direction: column;
    gap: 1rem;
  }

  section {
    padding: 3rem 1rem;
  }

  .services-grid, .features-grid {
    grid-template-columns: 1fr;
  }

  .process-grid {
    grid-template-columns: 1fr;
  }
}
</style>

