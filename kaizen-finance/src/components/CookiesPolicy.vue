<template>
  <div class="cookies-page">
    <section id="Cookie-Policy" class="in-view policy-section">
      <h1>Cookie Policy</h1>

      <h2>1. Introduction</h2>
      <p>
        Our website uses cookies to enhance user experience, analyze site usage, and assist in our marketing efforts.
        This policy explains what cookies are, how we use them, and how you can manage your preferences.
      </p>

      <h2>2. What Are Cookies?</h2>
      <p>
        Cookies are small text files placed on your device by websites you visit. They are widely used to make websites
        work efficiently and to provide information to site owners.
      </p>

      <h2>3. Types of Cookies We Use</h2>
      <ul>
        <li><strong>Strictly Necessary Cookies</strong>: These are essential for the website to function correctly, enabling services like account access and transaction processing.</li>
        <li><strong>Performance Cookies</strong>: These collect information about how visitors use our site, helping us improve functionality.</li>
        <li><strong>Functional Cookies</strong>: These remember your preferences to provide a personalized experience.</li>
        <li><strong>Targeting/Advertising Cookies</strong>: These track browsing habits to deliver relevant advertisements.</li>
      </ul>

      <h2>4. Third-Party Cookies</h2>
      <p>
        We collaborate with third-party service providers, such as financial product brokers, who may set cookies on our
        behalf to deliver tailored content and assess site performance.
      </p>

      <h2>5. Managing Your Cookie Preferences</h2>
      <p>
        Upon your first visit, you'll be presented with a cookie consent banner allowing you to accept or reject
        non-essential cookies. You can modify your preferences at any time through your browser settings or by
        revisiting the cookie settings on our website.
      </p>

      <h2>6. Data Protection and Privacy</h2>
      <p>
        We are committed to safeguarding your personal data. For detailed information on how we handle your data, please
        refer to our
        <router-link to="/privacy-policy#Privacy-Policy">Privacy Policy</router-link>.
      </p>

      <h2>7. Changes to This Policy</h2>
      <p>
        We may update this policy periodically. Any changes will be posted on this page with an updated effective date.
      </p>

      <h2>8. How to Delete Cookies</h2>
      <p>
        If you want to restrict or block the cookies that are set by our website, you can do so through your browser
        setting. Alternatively, you can visit
        <a href="https://support.google.com/chrome/answer/95647" target="_blank" rel="noopener noreferrer">this
          link</a>, which contains comprehensive information on how to do this on Google Chrome Browser. For other
        browser providers, you will find general information about cookies and details on how to delete cookies from your
        device through their website.
      </p>

      <h2>9. Contact Us</h2>
      <p>
        If you have questions about this policy, please contact us at
        <a href="mailto:info.kaizen4fs@gmail.com">info.kaizen4fs@gmail.com</a>.
      </p>
    </section>

    <footer class="cookies-footer">
      <div class="footer-content">
        <div class="policy-links">
          <router-link to="/privacy-policy#Privacy-Policy">Privacy Policy</router-link>
          <router-link to="/cookies-policy#Cookie-Policy">Cookies Policy</router-link>
        </div>
        <div class="copyright">© 2025 Kaizen Finance Solution Limited. All rights reserved.</div>
      </div>
    </footer>
  </div>
  
</template>

<script>
export default {
  name: 'CookiesPolicy'
}
</script>

<style scoped>
.policy-section {
  padding: 2em;
  background-color: #fff;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
  opacity: 1;
  border-radius: 3px;
  transform: translateY(0);
  transition: opacity 0.8s ease-out, transform 0.8s ease-out;
  margin: 2em auto;
  max-width: 800px;
}

.policy-section h1 {
  margin-top: 0;
}

.policy-section ul {
  padding-left: 1.2rem;
}

.cookies-footer {
  background-color: #ffffff;
  color: #000000;
  padding: 2rem 1rem;
  margin-top: 3rem;
  border-top: 1px solid #e6e6e6;
}

.footer-content {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1rem;
}

.policy-links {
  display: flex;
  gap: 2rem;
  flex-wrap: wrap;
  justify-content: center;
}

.policy-links a,
.policy-links :deep(a) { color: #000000; text-decoration: underline; font-size: 0.9rem; transition: opacity 0.3s ease; }

.policy-links a:hover,
.policy-links :deep(a:hover) {
  opacity: 0.8;
  text-decoration: underline;
}

.copyright { color: #000000; font-size: 0.8rem; text-align: center; margin-top: 1rem; }

@media (max-width: 600px) {
  .policy-links {
    flex-direction: column;
    align-items: center;
    gap: 1rem;
  }
}
</style>


