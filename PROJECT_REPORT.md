**ABSTRACT**

</div>

The **Cafe Management System** is a console-driven Java application backed by MySQL that replaces handwritten tickets and scattered spreadsheets with a single, menu-driven workflow. It covers the essential operational domains of a small café—customers, products, orders, billing, and basic reporting—while deliberately excluding graphical interfaces, cloud services, and external payment integrations. The purpose is to provide a predictable, low-cost alternative to paper processes that can run on shared café or lab laptops without additional licences or specialised hardware.

The scope is intentionally narrow to match a single-location café with intermittent connectivity and no dedicated DevOps support. All interactions occur through the command line; data is stored locally in MySQL; and configuration is consolidated in one properties file to simplify deployment and maintenance. Assumptions include modest transaction volumes, a small staff who can operate a CLI, and an environment where periodic manual backups are acceptable. Constraints explicitly rule out web front-ends, mobile clients, third-party gateways, and analytics tooling so that the system remains transparent, teachable, and easy to troubleshoot.

Development followed a straightforward engineering path: requirements were distilled from everyday café tasks, a lightweight layered design separated CLI menus from manager logic and data access, and the schema was defined around customers, products, orders, and order line items. Implementation used plain Java and JDBC with prepared statements to maintain data integrity, and transactions ensured that multi-item orders either committed fully or rolled back cleanly. Validation combined small seed datasets, repeatable test runs of CRUD flows, and manual end-to-end exercises on common operating systems to confirm that behaviour matched the specified constraints.

Early trial runs on sample data demonstrated faster order capture, clearer stock tracking, and quicker end-of-day reconciliation compared to the manual baseline. The system’s simplicity makes it suitable for coursework demonstration and for small cafés that value predictable workflows over feature breadth. This report documents the design choices, implementation steps, testing approach, assumptions, and identified areas for incremental growth, all within the confines of a CLI-only Java and MySQL stack.

---

<div align="center">

**TABLE OF CONTENTS**

</div>

- Acknowledgement
- Abstract
- Chapter 1: Introduction (Context, Scope, Approach)
- Chapter 2: Literature Review (POS Landscape, Technology Foundations, Lessons Learned)
- Chapter 3: System Analysis (Requirements, Feasibility, Models)
- Chapter 4: System Design (Architecture, Data and Interaction Design, Security Considerations)
- Chapter 5: Implementation (Stack, Key Behaviours, Deployment and Operations)
- Chapter 6: Testing and Validation (Strategy, Coverage, Outcomes)
- Chapter 7: Results, Evaluation, and Discussion (Operational Outcomes, Positioning, Limitations)
- Chapter 8: Project Management and Sustainability (Plan, Resources, Quality)
- Chapter 9: Conclusion and Future Work
- References
- Appendices

---

<div align="center">

**LIST OF FIGURES**

</div>

- Figure 1: Layered architecture overview for the CLI, managers, and data access
- Figure 2: Class diagram of manager and DAO interactions
- Figure 3: Entity-relationship diagram for customers, products, orders, and order items

---

<div align="center">

**LIST OF TABLES**

</div>

- Table 1: Requirements summary
- Table 2: Test plan summary

---

<div align="center">

**LIST OF SYMBOLS AND ABBREVIATIONS**

</div>

- CLI — Command Line Interface  
- CRUD — Create, Read, Update, Delete  
- DAO — Data Access Object  
- DBMS — Database Management System  
- ER — Entity Relationship  
- JDBC — Java Database Connectivity  
- OOP — Object-Oriented Programming  
- SDLC — Software Development Life Cycle  
- SQL — Structured Query Language  
- UML — Unified Modeling Language  

---

## CHAPTER 1: INTRODUCTION

### 1.1 Context and Goals

Local cafés frequently depend on handwritten tickets, informal notebooks, and ad hoc spreadsheets to keep track of customers, products, and daily sales. These improvised methods create delays during peak hours, increase the likelihood of billing mistakes, and make it difficult to reconcile inventory or cash at closing time. Staff often duplicate effort by copying totals from paper to spreadsheets, which introduces further errors and consumes time that could be spent serving customers. The need for a predictable, repeatable process therefore becomes a practical motivation rather than a theoretical one.

The **Cafe Management System (CMS)** is designed to replace these fragmented practices with a single, predictable workflow that lives entirely on the command line. By relying on a small set of artefacts—a menu-driven interface, a MySQL database, and one configuration file—the system remains easy to deploy on existing café laptops without additional licensing costs or specialised hardware. The goal is not to introduce complexity but to standardise routine tasks such as adding customers, updating product prices, creating orders, and issuing invoices so that day-to-day operations remain consistent even when staff rotate.

An additional goal is to make operational data immediately usable. Centralising records in MySQL means that order histories, stock adjustments, and customer details are stored in one place, allowing the café to review trends over time without re-entering data. Keeping the interface strictly CLI avoids distractions and aligns with the target environment where internet connectivity may be unreliable and graphical interfaces may not be available. The CMS therefore positions itself as a practical bridge from manual workflows to disciplined digital record-keeping without changing the technology footprint of a small café.

### 1.2 Scope and Constraints

The implemented scope is deliberately narrow to suit a single-location café that needs reliable record-keeping without extra infrastructure. The system provides a menu-driven CLI for creating and updating customer records, maintaining a product catalogue with prices, capturing orders with multiple line items, and issuing straightforward invoices. Simple summaries—such as daily totals or frequently ordered items—are generated from the same MySQL data, keeping all information consistent. The design avoids optional conveniences like graphical summaries or automated notifications so that the core experience remains stable and easy to understand.

Several constraints are explicit. There is no integration with payment gateways, loyalty programmes, reservation systems, or third-party services. The application is not a web app and does not assume continuous network access beyond the local database connection. Data protection relies on disciplined use of MySQL and periodic manual backups rather than managed cloud services. The target users are staff who are comfortable typing into a terminal but may not be familiar with database administration; consequently, configuration is consolidated into a single properties file that can be edited with minimal guidance.

The operational assumptions further shape the scope: one café location, a modest number of concurrent users, and shared computers that run the CLI locally. Because there is no dedicated DevOps team, maintenance tasks are kept simple—running the provided SQL setup script, performing occasional backups, and applying schema changes only when new features are explicitly added. These boundaries ensure the system remains realistic for its context while still delivering meaningful improvements over paper-based processes.

### 1.3 Approach and Report Map

The project followed a straightforward path tailored to the constraints of a small academic build. First, the core needs were gathered by mapping the everyday tasks of a café—adding products, recording customers, placing orders, and issuing bills—into a concise requirement checklist. That checklist drove an initial entity-relationship sketch for MySQL and a layered design separating the CLI menus from the manager classes and the data-access logic. The design remained intentionally light so that adjustments could be made quickly after initial trials.

Implementation proceeded incrementally: the database schema was created from the checklist, the CLI menus were wired to manager classes, and each manager invoked data-access methods that use prepared statements for safety. Two rounds of trial orders with sample data led to small schema refinements, such as enforcing positive prices and allowing for multiple items per order. Basic tests verified that CRUD flows worked end to end and that transactions rolled back correctly when an insert failed.

The remainder of this report mirrors that journey. The next chapter reviews related work to position the CMS among existing solutions. System analysis captures requirements, feasibility considerations, and simple models. System design explains the layered architecture and database choices. Implementation documents key modules and operational decisions. Testing and validation describe how the flows were exercised. Results and discussion interpret what the pilot runs demonstrated. Project management notes schedule, resources, and quality controls. The report concludes with limitations and a future roadmap that respects the same constraints outlined above.

---

## CHAPTER 2: LITERATURE REVIEW

### 2.1 Point-of-Sale Landscape

Commercial point-of-sale platforms such as Toast POS and Square for Restaurants provide extensive capabilities, ranging from integrated payments to marketing add-ons. However, these suites typically impose recurring subscription fees and assume dedicated hardware terminals, which are beyond the budget and needs of a small café that only requires reliable order capture and basic record-keeping. For academic settings, these commercial offerings also reduce opportunities for students to inspect and adapt the internals, limiting their value as learning artefacts.

Lightweight academic prototypes often prioritise graphical interfaces to mimic retail experiences, but those choices introduce additional complexity in setup and testing. A command-line approach, by contrast, aligns with low-cost lab environments where installing extra UI libraries is discouraged and where remote access over SSH is common. The CLI is therefore not a constraint but a deliberate fit for scenarios that demand easy deployment, scripting, and reproducible demonstrations without specialised peripherals.

Within this landscape, the presented Cafe Management System focuses on core operational needs—capturing customers, managing products, recording orders, and producing invoices—without attempting to replicate the breadth of commercial platforms. This narrower scope ensures the system remains maintainable, is easily audited in coursework, and can be executed on shared laboratory machines without additional procurement or licensing. The literature thus motivates a minimal, inspection-friendly solution rather than a feature-rich commercial substitute.

### 2.2 Technology Foundations

The project leans on foundational software engineering principles instead of external frameworks. Object-oriented design guides the separation of responsibilities into manager classes for business logic and data access objects for persistence. This modularity allows each concern—input validation, transaction handling, and SQL execution—to be reasoned about independently, simplifying maintenance in a small team environment. Relational database design principles ensure that entities such as customers, products, and orders are normalised, reducing duplication and preserving referential integrity in MySQL.

Human–computer interaction considerations, though modest in a CLI, remain essential. Clear prompts, confirmation messages before deletions, and validation of basic fields (for example, positive pricing and sensible phone numbers) reflect lessons from prior POS implementations that suffered from operator error. The absence of a GUI does not negate the need for usability; instead, it channels the effort into concise text interactions that staff can master quickly.

Java paired with JDBC is chosen because it is stable, widely taught, and ships with a rich standard library suitable for console applications. The use of plain JDBC keeps the stack transparent—SQL queries are explicit, and resource management is visible—making it easier for students to trace control flow without the abstraction layers of larger frameworks. Utility methods centralise recurring tasks such as opening connections and handling exceptions, reducing repetition while keeping the code readable and aligned with core language features.

### 2.3 Lessons from Prior Work

Prior student projects and small-scale restaurant tools highlight recurring success factors: keep the data model concise, validate inputs before hitting the database, and log significant actions for traceability. These findings inform this CMS by encouraging a compact schema, enforcing checks at the CLI layer, and recording key events without overwhelming operators with technical details. When data remains tidy and validation is consistent, later maintenance—such as correcting prices or updating customer records—becomes simpler and less error-prone.

Another lesson from earlier efforts is the importance of correctly handling transactions. Partial writes during order creation can leave orphaned records or mismatched totals, a problem observed in academic builds that lacked explicit rollback paths. To mitigate this, the CMS wraps multi-step order inserts in transactions and uses prepared statements to avoid malformed input, ensuring that either the entire order persists or nothing does. This practice keeps the database coherent even when the application encounters unexpected input or operator mistakes.

Finally, the literature notes that minimal logging and straightforward menu flows reduce operator training time. Instead of elaborate navigation, the CMS presents direct actions—add customer, add product, create order—mirroring the real-world tasks staff perform. This alignment with actual café workflows makes the system easier to adopt and less likely to be bypassed in favour of ad hoc paper processes, which is a common pitfall in deployments of overly complex academic prototypes.

---

## CHAPTER 3: SYSTEM ANALYSIS

### 3.1 Requirements Summary

Requirements were gathered directly from the everyday tasks of a single-location café. The essential capabilities include capturing basic customer details, maintaining a product catalogue with names and prices, placing orders that contain multiple line items, and printing invoices that reflect those items accurately. Operational staff also requested simple summaries—such as daily totals and frequently ordered products—to help with end-of-day reconciliation. These needs define the minimum viable scope and avoid optional conveniences that would increase complexity without clear benefit to the target environment.

Performance and usability expectations are modest but explicit. Lookups for customers and products should be responsive even on mid-range laptops, and menu navigation should be linear and predictable so new staff can learn the system quickly. Validation rules—such as enforcing positive prices and reasonable phone number formats—are treated as requirements because they prevent rework and reduce downstream corrections in the database.

Each requirement was paired with a brief test note, for example “verify order total matches sum of line items” or “reject negative prices.” This pairing kept development aligned with validation and ensured that the implemented features could be demonstrated consistently. By keeping the requirement set compact and testable, the project preserved focus on core café operations instead of drifting toward broader retail features.

### 3.2 Feasibility and Risks

Technical feasibility rests on familiar, widely available tools: Java 17 for the CLI and MySQL for storage. Both run reliably on common lab or café laptops without specialised hardware, and neither requires cloud services. This reduces deployment friction and keeps the environment under the café’s direct control. The application’s modest CPU and memory needs align with this constraint, ensuring that responsiveness remains acceptable during typical usage.

Several risks were identified early. Database downtime—whether from misconfiguration or power loss—could halt order entry; to mitigate this, regular SQL dumps are recommended and the application surfaces clear error messages when connections fail. Data quality risks emerge when importing legacy spreadsheets; a “dry run” mode for imports lets staff preview validation errors before committing changes. Training time is another risk because staff rotate; concise how-to notes and simple menu wording lower the onboarding burden.

Operational assumptions include the availability of at least one staff member who can restart the application and restore from a backup if necessary. There is no assumption of continuous internet access, so all functionality runs locally against MySQL. By constraining the environment to what is realistically available on-site, the project reduces external dependencies and focuses on robustness within those bounds.

### 3.3 Models Used

The analysis phase used a handful of lightweight models to align understanding before coding. A use case list enumerated core actions such as “add product,” “create order,” and “print invoice,” ensuring that each functional requirement had a clear operational expression. A context view showed the CMS in relation to the only external element—the MySQL database—reinforcing that no other services are in scope.

An entity-relationship (ER) diagram mapped customers, products, orders, and order line items, clarifying primary keys, foreign keys, and cardinalities. This helped confirm that one order could contain many products and that products could appear across many orders without duplicating product records. Because the system is limited to a single café, the model deliberately excludes branches, delivery partners, or payment processors.

Simple sequence notes captured the typical flow: a staff member selects “create order,” optionally adds or selects a customer, adds one or more products as line items, and then confirms to generate an invoice. Keeping these models brief makes them easy to update when modest enhancements are added, such as loyalty tracking or basic discounts, without needing to rework the entire analysis artefact set.

---

## CHAPTER 4: SYSTEM DESIGN

### 4.1 Architecture

The system adopts a simple three-layer structure that fits a console-only environment. At the top, CLI menus capture user choices and present concise prompts; in the middle, manager classes enforce business rules such as ensuring prices are positive and orders contain at least one item; at the base, DAO classes handle direct interactions with MySQL. This separation keeps the code readable and ensures that changes to database queries do not affect user-facing menus. It also means that if a different interface is ever added, the underlying logic remains intact.

Each layer communicates through clearly defined method calls rather than shared global state. This choice reduces coupling and makes it straightforward to trace a request from the menu selection down to the SQL execution. Because the system runs on shared café laptops, the architecture avoids background daemons or auxiliary services that would complicate deployment. All configuration—such as database host, port, and credentials—is externalised in a single properties file, allowing the application to be re-pointed to another MySQL instance without recompilation.

The architecture explicitly omits additional tiers like message queues, caching servers, or web front-ends because the target usage does not warrant them. By constraining the design to three layers, the project maintains a balance between structure and simplicity, enabling students and café staff to understand and troubleshoot the flow without specialised tooling.

### 4.2 Data and Interaction Design

The relational schema connects four core entities: customers, products, orders, and order line items. Foreign keys enforce that every order line references a valid product and that every order can be tied to an optional customer. Indexes on customer identifiers, product identifiers, and order dates are chosen to accelerate common lookups, such as listing recent orders or checking whether a customer already exists before inserting a duplicate.

User interactions follow a predictable path: a menu choice triggers a manager method, the manager validates the input, and the DAO executes the SQL. On success, a concise confirmation is shown; on failure, the user sees a brief message while the log records technical details. Validation rules—such as ensuring phone numbers meet a basic length threshold and prices are greater than zero—run before database calls to prevent unnecessary transactions and to keep constraints easy to understand.

Because the system is CLI-only, navigation remains linear. Menus are arranged so that staff can complete tasks in minimal steps, and lists are kept short with simple pagination when product counts grow. This interaction design reflects the constraint of operating without a GUI while still providing clear feedback loops for each action.

### 4.3 Security and Growth Hooks

Security measures focus on what is practical for a single-location CLI deployment. Database credentials are stored outside source control so they can be rotated without code changes. All SQL uses prepared statements to guard against injection in user inputs such as names or phone numbers. Error logs intentionally avoid printing credentials or raw SQL, reducing the risk of accidental exposure on shared machines.

Data protection relies on straightforward practices: periodic MySQL dumps for backups, simple database roles with only the permissions needed by the application, and clear operator instructions for restore procedures. Because there is no web surface, the threat model is limited to local misuse or misconfiguration, so controls emphasise principle of least privilege and recoverability rather than perimeter defences.

Potential growth areas are documented but not enabled by default. If order volume rises, connection pooling could be added to reuse database connections, and basic caching could reduce repeated product lookups. These remain optional to keep the current deployment lean and debuggable, aligning with the constraint of a small café without dedicated infrastructure.

---

## CHAPTER 5: IMPLEMENTATION

### 5.1 Stack and Modules

The implementation relies on a small, stable toolchain: Java 17 for the CLI application, Maven for builds and dependency management, and MySQL 8.0 with Connector/J for database access. This selection avoids third-party frameworks and keeps the runtime easy to reproduce on typical lab or café laptops. Each dependency is widely available, well-documented, and familiar to undergraduate courses, reducing setup friction and making the code easier to review.

Core classes are organised by responsibility. `DatabaseConnection` handles driver loading, connection acquisition, and optional bootstrap of the schema; it includes simple retry logic to cope with transient startup issues. `CustomerManager`, `ProductManager`, and `OrderManager` encapsulate business rules for their respective domains—validating inputs, orchestrating transactions, and delegating persistence to DAO methods. A minimal logger records key events and exceptions to aid debugging without overwhelming operators with technical details.

Configuration is read once at startup from a properties file that specifies database host, port, schema, user, and password. This keeps deployment flexible without recompiling. The schema bootstrap script (`database_setup.sql`) can be executed manually or via a helper method on first run, ensuring that tables and basic constraints exist before regular use. No background services are required; the application runs in a single process, matching the simplicity goals of the project.

### 5.2 Key Behaviours

User actions begin at the CLI menus, which map directly to manager methods. Each manager validates incoming data—ensuring, for example, that prices are positive, quantities are non-zero, and phone numbers meet basic length rules—before invoking DAO calls. This validation at the edge reduces invalid SQL operations and keeps feedback immediate for the operator.

Order creation is treated as a multi-step transaction: adding the order header, inserting each line item, and committing only if all inserts succeed. If any step fails, the transaction rolls back to avoid partial orders and inconsistent totals. Duplicate customer detection uses phone numbers as a practical identifier, reducing repeated entries while accommodating cafés that may not capture email addresses.

List views apply simple pagination to keep output readable when the product catalogue grows. Errors are communicated with brief, plain-language messages so staff understand what to do next, while detailed stack traces and SQL errors are written to logs for troubleshooting. This balance keeps the CLI approachable without hiding diagnostic information from maintainers.

### 5.3 Deployment and Operations

Deployment is intentionally lightweight. The application runs directly from the command line; after configuring the properties file, a user executes the jar, and the system connects to MySQL using the provided credentials. The `database_setup.sql` script seeds tables and constraints for first-time installations and can be rerun safely on a fresh schema.

For classroom demonstrations or repeatable grading, the provided Dockerfiles allow the app and MySQL to be containerised together, but this is optional and not a runtime dependency. Routine backups rely on standard MySQL dump commands scheduled via cron or a simple batch file—no cloud services or external schedulers are assumed. Environment variables can override credentials when running in shared labs to avoid editing configuration files directly, which simplifies resets between sessions.

Operational tasks are kept to a minimum: start the database, run the application, and perform periodic dumps. There is no background daemon or web server to monitor. This approach aligns with the constraint of a small café or academic lab where dedicated operations staff are not available.

---

## CHAPTER 6: TESTING AND VALIDATION

### 6.1 Strategy

The testing approach was scoped to what a single-location CLI system must reliably support. Core CRUD paths for customers, products, and orders were exercised to confirm that every menu action performs the intended database operation without leaving partial data. Particular emphasis was placed on order creation because it involves multiple inserts; the strategy required that either all line items and the header commit together or none do, preserving invoice correctness.

Validation scenarios were treated as first-class test cases. Inputs with negative prices, implausible phone lengths, or zero-quantity line items were supplied to ensure the application rejected them before hitting the database. Duplicate customer creation using the same phone number was attempted to verify that the system prevents redundant records and keeps the customer list clean. These checks align with the design decision to enforce simple business rules at the CLI layer.

Repeatability was maintained by resetting data between runs. A small, known seed dataset enabled comparisons of expected versus actual outcomes, and cleanup scripts restored the database to a baseline state to avoid cross-test contamination. This discipline ensured that observed behaviours stemmed from the code under test rather than leftover data from prior executions.

### 6.2 Coverage and Tools

JUnit provided unit-level assurance for helper methods and validation routines, while manual end-to-end passes confirmed that the CLI menus, managers, and DAOs worked together on both macOS and Windows using the same Java/MySQL versions. This dual-platform check was important because the deployment environment may vary across café laptops or lab machines.

Prepared statements were intentionally stressed with malformed inputs to confirm that parameter binding rejects unsafe data and that meaningful errors surface without exposing SQL details to the user. Basic timing spot-checks on sample datasets—hundreds of products and dozens of orders—ensured that order creation and lookups stayed responsive within a console context, even without performance tuning.

Coverage focused on functional correctness and safety rather than load or concurrency, reflecting the assumption of a modest user base and single-site deployment. No GUI or web tooling was involved, consistent with the project constraints; all observations came from the CLI output and log files generated during test runs.

### 6.3 Outcomes

Results showed that the intended flows operated correctly once two early issues were resolved: an input validation edge case on phone numbers and a rollback path that initially failed to undo partial order inserts. After addressing these, CRUD operations and transaction boundaries behaved as designed, leaving the database in a consistent state after each scenario.

Manual sessions highlighted minor usability refinements, such as clearer wording on menu labels and confirmation prompts before deletions, which were incorporated into the CLI text. Logs captured stack traces and SQL errors as planned, aiding quick diagnosis without confusing operators with technical output.

Future improvements to testing are documented for later iterations: scripted regression suites to avoid reliance on manual runs, seed data factories to accelerate setup, and light load checks to observe behaviour with larger product lists. These steps remain aligned with the current constraints—no GUI, no web tier, no external services—while raising confidence as the codebase evolves.

---

## CHAPTER 7: RESULTS, EVALUATION, AND DISCUSSION

### 7.1 Operational Outcomes

Pilot runs with seeded data and mock orders showed that the command-line menus reduced order entry to a predictable sequence of prompts, cutting out the back-and-forth that occurred with handwritten tickets. Staff could follow the same sequence every time—select customer, pick products, confirm quantities—and the resulting invoice text reflected the exact inputs without later copying into spreadsheets. Because the workflow is linear and free from modal dialogs or hidden shortcuts, novice operators reported they could repeat the process after one walkthrough, which aligns with the project goal of keeping training burden low.

End-of-day reconciliation improved because totals are generated directly from stored orders rather than manual tally sheets. With seeded datasets, the CMS produced daily revenue, item counts, and frequent-customer lists in a few minutes, whereas the baseline process required manual calculator work that was prone to transcription mistakes. Inventory adjustments also became traceable: each decrement ties to an order line, which eliminated ambiguous free-text notes like “-2 milk” that lacked context. This traceability made it easier to explain variances when demonstrating the system to peers acting as supervisors.

Short surveys with classmates role-playing staff highlighted small usability refinements. They asked for clearer wording around discounts and voids to avoid accidental double-entry, and they suggested more explicit confirmation messages before irreversible actions. These notes were recorded as text copy edits rather than feature changes, maintaining the constraint of a pure CLI without introducing new UI elements or workflows. Observations from these pilots are feeding into minor wording updates and help prompts, not structural changes to the system.

### 7.2 Market and Financial Positioning

Benchmarking against commercial POS solutions focused on cost, customisability, offline resilience, and data ownership because those dimensions matter most to a single-site café without dedicated IT staff. Commercial suites deliver integrated payments, loyalty, and marketing, but they impose recurring subscription fees and depend on vendor-managed infrastructure. The CMS, by contrast, runs entirely on existing laptops with MySQL and Java, so there are no licence costs and no dependency on vendor uptime, aligning with the requirement to stay operational even if connectivity is intermittent.

Customisability was another differentiator. Because the codebase is small and uses only standard libraries, coursework-driven tweaks—such as adjusting validation rules or changing report formats—can be made quickly without navigating proprietary APIs. This makes the system suitable for academic evaluation, where transparency and modifiability are valued over breadth of features. Data ownership remains local to the café; all records sit in MySQL under the operator’s control, which avoids concerns about exporting or deleting data from third-party clouds.

A brief cost note compared two deployment options already in scope: running MySQL locally versus hosting it on a low-cost VM. The default recommendation stays with local installs to preserve offline capability and avoid hosting fees, but the VM option was analysed to show that a migration path exists if remote access becomes necessary later. Even in that scenario, the stack remains identical—Java CLI plus MySQL—so no new technologies are introduced, and operational costs stay predictable.

### 7.3 Sustainability and Limitations

Sustainability in this context is practical rather than architectural. By recording every order and associating products with quantities, the CMS helps avoid over-ordering perishables; staff can review which items actually sell and trim excess stock, reducing waste. The ability to issue simple digital receipts from the CLI reduces paper use during demonstrations without requiring a printer, which aligns with the low-footprint goal of the project. Because the application idles with minimal resource use, it fits cafés that run on standard laptops without additional energy overhead.

Several limitations are explicit and retained to keep the system realistic. There is no integrated payment gateway, so all payments remain cash or card processes handled outside the application. The interface is entirely command-line, which limits accessibility for users who prefer graphical cues, and multilingual support is minimal because prompts are in English only. Backup and recovery rely on manual MySQL dumps rather than automated schedulers; this keeps the tooling simple but requires operator discipline.

Future scope stays within the constraints of the current stack. Documenting a repeatable backup-and-restore drill for staff is a near-term improvement that does not add new technologies. Optional next steps include clearer email receipt instructions using existing system tools and, if future coursework permits, a lightweight web front-end that reuses the same managers and database schema; such an interface would be additive rather than replacing the CLI. These items are logged as future enhancements, not as present features, so the current deployment remains a straightforward Java CLI over MySQL.

---

## CHAPTER 8: PROJECT MANAGEMENT AND SUSTAINABILITY

### 8.1 Plan and Schedule

Work was split into requirements, design, build, test, and handoff phases to mirror a lightweight SDLC suitable for a single-student project. A 16-week schedule was chosen because it fit the academic term and allowed buffer weeks for integration issues without resorting to overtime. Weekly check-ins with the mentor replaced heavyweight project management tools, ensuring that scope changes were discussed early and that the project stayed aligned with the original constraints of a CLI-only Java/MySQL solution.

The timeline emphasised front-loading clarity: 3 weeks were spent refining requirements and drafting the schema so that later coding would not need major rework. Implementation of core modules occupied 5 weeks, during which database access, menu flows, and transaction handling were built iteratively and verified against the requirement checklist. Testing and fixes consumed the next 3 weeks, focusing on validation edge cases and transaction rollbacks, while documentation and packaging filled the final 2 weeks. Remaining buffer weeks were reserved for integration hiccups, such as aligning SQL scripts with the running MySQL instance on different laptops.

Milestones were deliberately small and demonstrable: “CRUD for customers complete,” “orders commit atomically,” and “seed data loads without errors.” Each milestone required a brief walkthrough to the mentor, substituting for formal stage gates. This approach kept progress visible without introducing new tools or processes, consistent with the goal of keeping the project simple and reproducible in a lab or café setting.

### 8.2 Resources and Budget

Human resources remained minimal: the student developer handled implementation and testing, while the mentor provided weekly feedback and occasional peer reviews offered a second opinion on usability. This small team size reduced coordination overhead but required disciplined note-taking to capture decisions about validation rules and schema changes. Tooling stayed within standard, no-cost options—Java 17, MySQL, and community IDEs—avoiding any licences that would complicate reuse by classmates.

The budget considerations reflected the low-footprint intent of the system. Hardware consisted of existing laptops; software costs were effectively zero because all dependencies are open source or provided by the university. Optional expenses included printing excerpts of the report for review sessions and brief cloud trials for MySQL, though the default remained local installations to preserve offline capability. Any such trials were kept within free tiers, and no paid services were adopted, consistent with the constraint against new technologies or external platforms.

Operational risks tied to shared lab machines and exam periods were addressed by keeping a portable development setup: the schema script, properties file, and jar could be copied to another machine and run without reinstallation of niche tools. Regular MySQL dumps and git snapshots (when available) protected progress against hardware issues or accidental deletions. These safeguards relied only on built-in utilities, matching the project’s simplicity goals.

### 8.3 Quality and Compliance

Quality controls were kept lightweight but consistent. Small checklists accompanied each commit: verify CRUD flows for customers, products, and orders; confirm transaction rollback on failed inserts; and re-run seed data loads to ensure schemas stayed aligned. Peer or mentor reviews focused on clarity of prompts and validation messages because usability depends heavily on precise wording in a CLI. Regression checks were manual but repeatable, reflecting the modest scope and absence of automation frameworks.

Compliance considerations centred on data handling and licensing. Test datasets used anonymised placeholders to avoid storing real customer information, and logs were configured to exclude sensitive fields such as credentials. All third-party components (for example, MySQL Connector/J) carry permissive licences, and acknowledgements are listed in Appendix C to satisfy academic citation requirements. The project avoids any data export to external services, so data ownership remains entirely with the café or the lab environment.

Operational compliance was addressed through simple, documented routines. A short README section explains how to perform MySQL backups and restores using standard commands, ensuring that staff or graders can recover from errors without specialised tools. Because there is no GUI or web surface, security concerns focus on credential handling and principle of least privilege in the database user account. These measures keep the system aligned with campus expectations while preserving the minimal, CLI-first design.

---

## CHAPTER 9: CONCLUSION AND FUTURE WORK

### 9.1 Conclusion

The **Cafe Management System** replaces scattered manual steps with a single, predictable workflow for orders, products, and customers. The project demonstrates that a lean, CLI-only approach—grounded in Java and MySQL—can meaningfully improve consistency without adding operational overhead or licensing costs. By keeping the interface text-based and the database local, the solution remains aligned with the realities of a single café that relies on shared laptops and intermittent connectivity.

The development process followed straightforward steps: clarify needs drawn from day-to-day café tasks, sketch a minimal layered design, implement only the required flows, and validate behaviour against those same needs. This discipline prevented scope creep into web or mobile features and kept the codebase small enough to be understood quickly by peers. The resulting application shows that predictable menus, basic validation, and transactional order handling are sufficient to replace handwritten tickets while staying within tight constraints.

Overall, the CMS meets its primary objectives: it streamlines order capture, reduces reconciliation errors, and provides a baseline for future incremental enhancements. The outcome is intentionally modest—no cloud services, no GUI, and no external integrations—because reliability and maintainability for a single site were prioritised over breadth of features. This positioning makes the system a practical teaching artefact and a workable tool for a small café.

### 9.2 Contributions

Key contributions include a small modular architecture that separates CLI menus, manager logic, and data access so that each layer can be modified independently without side effects. The repeatable schema initialisation script ensures that any lab machine or café laptop can be brought to a known-good state quickly, reducing setup friction. Input validation is embedded at the CLI edge to stop bad data—negative prices, zero quantities, implausible phone numbers—before it reaches MySQL, reinforcing data quality with minimal code.

Documentation and operational notes were treated as first-class outputs. Clear instructions for configuring the properties file, running the SQL setup, and executing the jar mean that peers can reproduce results without extra tools. The testing approach, though lightweight, ties directly to requirements, showing how to exercise CRUD flows, transactions, and seed data loads on both macOS and Windows. Collectively, these elements form a cohesive template that future coursework can extend while staying transparent and easy to audit.

### 9.3 Future Enhancements

Future work remains within the existing CLI-and-MySQL stack to preserve the project’s simplicity. One priority is strengthening operational resilience: documenting and rehearsing a backup-and-restore drill, adding clearer CLI help text for error recovery, and shipping seed data packs to speed classroom demonstrations. Another is deepening test coverage with a small regression script that exercises core CRUD and transaction paths after each code change, improving confidence without new tools.

Reporting can be made more useful while staying text-based. Examples include richer on-demand summaries (top-selling items over a chosen date range) and optional CSV exports generated directly from the CLI for offline analysis. Usability refinements—such as more explicit prompts for discounts or voids, and optional confirmation steps before destructive actions—can be added without altering the architecture. All proposed enhancements avoid new technologies, GUIs, or external services, ensuring the system remains a straightforward Java CLI over MySQL.

---

## REFERENCES

1. Oracle Corporation, *Java Platform Standard Edition 17 Documentation*, 2025.  
2. Oracle Corporation, *MySQL 8.0 Reference Manual*, 2025.  
3. Bloch, J., *Effective Java (3rd Edition)*, Addison-Wesley Professional, 2018.  
4. Fowler, M., *Patterns of Enterprise Application Architecture*, Addison-Wesley Professional, 2002.  
5. Gamma, E. et al., *Design Patterns: Elements of Reusable Object-Oriented Software*, Addison-Wesley, 1994.  
6. Sommerville, I., *Software Engineering (10th Edition)*, Pearson, 2020.  
7. Pressman, R. S., *Software Engineering: A Practitioner’s Approach (9th Edition)*, McGraw-Hill, 2020.  
8. OWASP Foundation, *Application Security Verification Standard*, Version 4.0.3, 2024.  
9. JMeter Team, *Apache JMeter User Manual*, Apache Software Foundation, 2025.  
10. Toast POS, *Product Overview*, 2025.  
11. Square Inc., *Square for Restaurants Documentation*, 2025.  
12. Lightspeed, *Restaurant POS Features*, 2025.  
13. SRM Institute of Science and Technology, *Project Report Guidelines*, 2024.  
14. HikariCP, *Connection Pooling Best Practices*, 2025.  
15. IEEE, *SWEBOK: Guide to the Software Engineering Body of Knowledge*, 2024.

---

## APPENDICES

### Appendix A: Sample CLI Screenshots

1. Main menu with customer, product, and order modules.  
2. Order creation workflow highlighting validation prompts.  
3. Administrative report summary output.

### Appendix B: Deployment Artefacts

- Dockerfile for containerised deployment.  
- Docker Compose configuration for MySQL and application services.  
- Shell script for automated backups and log rotation.

### Appendix C: User Training Materials

- Quick-start guide for café staff.  
- Troubleshooting checklist for common issues.  
- Maintenance schedule for database backups and updates.

### Appendix D: UML Diagrams

Use Case Diagram (PlantUML):
```
@startuml
actor Staff
rectangle "Cafe Management System (CLI)" {
	usecase "Manage Customers" as UC1
	usecase "Manage Products/Menu Items" as UC2
	usecase "Place Order" as UC3
	usecase "Generate Bill" as UC4
	usecase "Update Inventory" as UC5
	usecase "View Basic Reports" as UC6
}

Staff --> UC1
Staff --> UC2
Staff --> UC3
Staff --> UC4
Staff --> UC5
Staff --> UC6
UC3 --> UC4 : includes
@enduml
```
Purpose: Single actor (Staff) interacts with core CLI features—customer/product CRUD, orders, billing, inventory updates, and basic reports—matching the local Java + MySQL scope.

Class Diagram (PlantUML):
```
@startuml
class App {
	+main(args)
}

class CustomerManager {
	+addCustomer(...)
	+updateCustomer(...)
	+listCustomers()
	+deleteCustomer(...)
}

class ProductManager {
	+addProduct(...)
	+updateProduct(...)
	+listProducts()
	+deleteProduct(...)
	+adjustStock(...)
}

class OrderManager {
	+createOrder(customer, items)
	+addItem(orderId, productId, qty)
	+finalizeOrder(orderId)
	+generateBill(orderId)
}

class DatabaseConnection {
	+getConnection(): Connection
}

class Order {
	-id
	-customerId
	-total
}

class OrderItem {
	-orderId
	-productId
	-quantity
	-price
}

App --> CustomerManager
App --> ProductManager
App --> OrderManager
CustomerManager --> DatabaseConnection
ProductManager --> DatabaseConnection
OrderManager --> DatabaseConnection
OrderManager --> Order
Order --> OrderItem
@enduml
```
Purpose: Minimal CLI architecture—App drives managers; managers validate and call JDBC via DatabaseConnection; Order and OrderItem model multi-line orders; no extra layers or frameworks.

Sequence Diagram (Place Order, PlantUML):
```
@startuml
actor Staff
participant "CLI (App)" as CLI
participant "OrderManager" as OM
participant "DatabaseConnection" as DB
database "MySQL" as SQL

Staff -> CLI : select "Place Order"
CLI -> OM : createOrder(customer, items)
OM -> DB : getConnection()
DB -> SQL : begin transaction
OM -> SQL : INSERT Order (header)
OM -> SQL : INSERT OrderItems (each line)
OM -> SQL : UPDATE Products SET stock = stock - qty
SQL --> DB : commit
DB --> OM : success
OM --> CLI : orderId, total, bill text
CLI --> Staff : display bill/confirmation
@enduml
```
Purpose: Shows transactional flow for placing an order via CLI; all inserts/updates run in one JDBC transaction, stock is decremented, and bill text is returned for display.

