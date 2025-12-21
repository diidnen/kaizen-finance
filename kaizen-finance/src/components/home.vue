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
          With our professional expertise and industry accreditations, we provide comprehensive financial and business solutions tailored to your needs.
        </p>
        <div class="services-grid">
          <div class="service-card">
            <i class="fas fa-chart-line"></i>
            <h3>Accounting & Tax</h3>
            <p>Clear, compliant accounting  and strategic tax planning to free your time, reduce your worry, and drive your growth.</p>
          </div>
          <div class="service-card">
            <i class="fas fa-coins"></i>
            <h3> Business Consultancy</h3>
            <p>Empowering your business with smart support and strategic insight—so you stay focused, productive, and poised for success
            </p>
          </div>
          <div class="service-card">
            <i class="fas fa-building"></i>
            <h3>Corporate and property Finance </h3>
            <p>Expert financial solutions for individuals and businesses of all sizes</p>
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
            <h3>Proven Expertise</h3>
            <p>Decades of insight turning complex finance into clear results.</p>
          </div>
          <div class="feature-card">
            <i class="fas fa-users"></i>
            <h3>Trusted Experience</h3>
            <p>A track record of success you can rely on.</p>
          </div>
          <div class="feature-card">
            <i class="fas fa-globe"></i>
            <h3>Client-Focused Strategies</h3>
            <p>Solutions built around your goals, not ours.</p>
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

    <!-- Testimonials Section -->
    <section class="testimonials-section">
      <div class="section-content">
        <div class="testimonials-header">
          <h2>Happy Customers</h2>
          <h3>See what our customers say</h3>
        </div>
        
        <div class="testimonials-container">
          <div class="testimonials-slider" ref="slider">
            <div 
              v-for="testimonial in testimonials" 
              :key="testimonial.id"
              class="testimonial-card"
            >
              <div class="quote-icon">"</div>
              <p class="testimonial-text">{{ testimonial.testimonial }}</p>
              <div class="customer-info">
                <div class="avatar">
                  <img 
                    v-if="testimonial.avatarUrl" 
                    :src="testimonial.avatarUrl" 
                    :alt="testimonial.customerName"
                    @error="handleImageError"
                  />
                  <div v-else class="avatar-placeholder">
                    {{ testimonial.customerName.charAt(0).toUpperCase() }}
                  </div>
                </div>
                <div class="customer-details">
                  <h4>{{ testimonial.customerName }}</h4>
                  <p>{{ testimonial.companyName }}</p>
                </div>
              </div>
            </div>
          </div>
          
          <!-- Navigation arrows -->
          <button class="nav-arrow nav-left" @click="previousTestimonial" :disabled="currentIndex === 0">
            <i class="fas fa-chevron-left"></i>
          </button>
          <button class="nav-arrow nav-right" @click="nextTestimonial" :disabled="currentIndex >= testimonials.length - 3">
            <i class="fas fa-chevron-right"></i>
          </button>
        </div>
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
import { ref, onMounted } from 'vue'
import http from '@/utils/request'

const testimonials = ref([])
const currentIndex = ref(0)
const slider = ref(null)

// 获取评价数据
const fetchTestimonials = async () => {
  try {
    const response = await http.get('/api/testimonials')
    if (response.code === 200) {
      testimonials.value = response.testimonials
    }
  } catch (error) {
    console.error('Failed to fetch testimonials:', error)
  }
}

// 处理图片加载错误
const handleImageError = (event) => {
  event.target.style.display = 'none'
  event.target.nextElementSibling.style.display = 'flex'
}

// 导航功能
const nextTestimonial = () => {
  if (currentIndex.value < testimonials.value.length - 3) {
    currentIndex.value++
    updateSliderPosition()
  }
}

const previousTestimonial = () => {
  if (currentIndex.value > 0) {
    currentIndex.value--
    updateSliderPosition()
  }
}

const updateSliderPosition = () => {
  if (slider.value) {
    const cardWidth = 320 // 卡片宽度 + 间距
    slider.value.style.transform = `translateX(-${currentIndex.value * cardWidth}px)`
  }
}

onMounted(() => {
  fetchTestimonials()
})
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
