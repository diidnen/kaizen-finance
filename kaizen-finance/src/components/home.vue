<template>
  <div class="home-container">
    <!-- Hero Section -->
    <section class="hero">
      <div class="hero-content">
        <h1>Accounting, Finance &amp; Tax</h1>
        <p>Based in London. We help with accounting, tax, mortgages, company set-up and business finance.</p>
        <div class="hero-contact">
          <router-link to="/contact" class="cta-button">CONTACT US</router-link>
        </div>
      </div>
    </section>

    <!-- Services Section -->
    <section class="services-section">
      <div class="section-content">
        <h2>Our Services</h2>
        <div class="services-grid">
          <div class="service-card">
            <i class="fas fa-chart-line"></i>
            <h3>Accounting &amp; Tax</h3>
            <p>Bookkeeping, VAT returns, payroll, year-end accounts and tax filing.</p>
          </div>
          <div class="service-card">
            <i class="fas fa-coins"></i>
            <h3>Business Consultancy &amp; MIS</h3>
            <p>MIS covers management (processes and controls), information (reporting, efficiency and company strategy) and systems (IT infrastructure and automation). It is broader than management accounting, which is mainly internal and focused on process efficiency.</p>
          </div>
          <div class="service-card">
            <i class="fas fa-building"></i>
            <h3>Finance</h3>
            <p>Commercial loans, mortgages, buy-to-let and property finance.</p>
          </div>
        </div>
        <router-link to="/services" class="secondary-button">FIND OUT MORE</router-link>
      </div>
    </section>

    <!-- Contact Section -->
    <section class="contact-section">
      <div class="section-content">
        <h2>Contact Us</h2>
        <p class="section-description">
          Email or call us. Free initial consultation.
        </p>
        <div class="contact-details">
          <p><i class="fas fa-envelope"></i> <a href="mailto:Info@kaizensolution.co.uk">Info@kaizensolution.co.uk</a></p>
          <p><i class="fas fa-map-marker-alt"></i> London, UK</p>
          <p><i class="fas fa-clock"></i> Monday – Friday, 9:00 AM – 6:00 PM</p>
        </div>
        <router-link to="/contact" class="cta-button">GET IN TOUCH</router-link>
      </div>
    </section>

    <!-- Footer -->
    <div class="site-footer">
      <div class="footer-inner">
        <div class="footer-brand">
          <img src="@/assets/final.png" alt="Kaizen Finance" />
        </div>
        <div class="footer-links">
          <router-link to="/privacy-policy">Privacy Policy</router-link>
          <router-link to="/cookies-policy">Cookies Policy</router-link>
        </div>
        <div>
          © 2025 Kaizen Finance Solution Limited. All rights reserved.
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
</script>

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

.contact-details {
  margin-bottom: 2rem;
}

.contact-details p {
  font-size: 1.1rem;
  color: #555;
  margin-bottom: 0.8rem;
}

.contact-details i {
  color: #007bff;
  margin-right: 0.5rem;
  width: 1.2rem;
}

.contact-details a {
  color: #007bff;
  text-decoration: none;
}

.contact-details a:hover {
  text-decoration: underline;
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

/* Testimonials Section */
.testimonials-section {
  background: #ffffff;
  padding: 80px 0;
}

.testimonials-header {
  text-align: center;
  margin-bottom: 60px;
}

.testimonials-header h2 {
  font-size: 0.9rem;
  font-weight: 600;
  color: #2c3e50;
  text-transform: uppercase;
  letter-spacing: 2px;
  margin-bottom: 10px;
}

.testimonials-header h3 {
  font-size: 2.5rem;
  font-weight: 700;
  color: #2c3e50;
  margin: 0;
}

.testimonials-container {
  position: relative;
  overflow: hidden;
  max-width: 1200px;
  margin: 0 auto;
}

.testimonials-slider {
  display: flex;
  transition: transform 0.35s ease;
  gap: 24px;
}

.testimonial-card {
  flex: 0 0 320px;
  background: #ffffff;
  border-radius: 14px;
  padding: 28px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.06);
  border: 1px solid #eef1f4;
  position: relative;
  min-height: 260px;
  display: flex;
  flex-direction: column;
}

.quote-icon {
  font-size: 3.5rem;
  color: #e6eef6;
  font-weight: 700;
  line-height: 1;
  margin-bottom: 16px;
}

.testimonial-text {
  font-size: 1.05rem;
  line-height: 1.8;
  color: #2b3a46;
  margin-bottom: 22px;
  flex-grow: 1;
  font-family: "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
  letter-spacing: 0.1px;
}

.customer-info {
  display: flex;
  align-items: center;
  gap: 15px;
}

.avatar {
  width: 52px;
  height: 52px;
  border-radius: 50%;
  overflow: hidden;
  flex-shrink: 0;
  border: 2px solid #eef1f4;
}

.avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.avatar-placeholder {
  width: 100%;
  height: 100%;
  background: #2c3e50;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.2rem;
  font-weight: bold;
}

.customer-details h4 {
  font-size: 1.05rem;
  font-weight: 700;
  color: #1f2d3a;
  margin: 0 0 6px 0;
  font-family: "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
}

.customer-details p {
  font-size: 0.9rem;
  color: #666;
  margin: 0;
}

.nav-arrow {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  background: #ffffff;
  border: 1px solid #e6e6e6;
  border-radius: 50%;
  width: 44px;
  height: 44px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.2s ease;
  z-index: 10;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
}

.nav-arrow:hover:not(:disabled) {
  background: #f7f9fb;
  color: #2c3e50;
  border-color: #d9e2ec;
}

.nav-arrow:disabled {
  opacity: 0.3;
  cursor: not-allowed;
}

.nav-left { left: -18px; }

.nav-right { right: -18px; }

.nav-arrow i { font-size: 0.95rem; }

/* 响应式设计 */
@media (max-width: 768px) {
  .testimonials-header h3 {
    font-size: 2rem;
  }
  
  .testimonial-card {
    flex: 0 0 280px;
    padding: 25px;
  }
  
  .testimonials-slider {
    gap: 15px;
  }
  
  .nav-arrow {
    width: 40px;
    height: 40px;
  }
  
  .nav-left {
    left: -20px;
  }
  
  .nav-right {
    right: -20px;
  }
}
</style>

<style scoped>
/* Footer styles appended */
.site-footer {
  background: #ffffff;
  color: #000000;
  padding: 30px 16px;
  border-top: 1px solid #e6e6e6;
}

.footer-inner {
  max-width: 1200px;
  margin: 0 auto;
  text-align: center;
}

.footer-links {
  display: flex;
  gap: 24px;
  justify-content: center;
  margin-bottom: 12px;
}

.footer-links a { color: #000000; text-decoration: underline; }

.footer-brand {
  display: flex;
  justify-content: center;
  margin-bottom: 12px;
}

.footer-brand img {
  height: 40px;
  width: auto;
}
</style>
