--
-- PostgreSQL database dump
--

-- Dumped from database version 15.5 (Homebrew)
-- Dumped by pg_dump version 15.5 (Homebrew)

-- Started on 2024-01-18 19:59:32 MSK

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 226 (class 1259 OID 275064)
-- Name: event; Type: TABLE; Schema: public;
--

CREATE TABLE public.event (
                              id bigint NOT NULL,
                              date date,
                              event_type integer,
                              min_age integer,
                              name oid
);

--
-- TOC entry 227 (class 1259 OID 275069)
-- Name: ticket; Type: TABLE; Schema: public;
--

CREATE TABLE public.ticket (
                               id bigint NOT NULL,
                               coordinate_x bigint,
                               coordinate_y integer,
                               creation_date date,
                               discount double precision,
                               name oid,
                               price double precision,
                               type integer,
                               event_id bigint
);

--
-- TOC entry 3641 (class 0 OID 275064)
-- Dependencies: 226
-- Data for Name: event; Type: TABLE DATA; Schema: public;
--

COPY public.event (id, date, event_type, min_age, name) FROM stdin;
10006	2024-01-11	2	12	275984
10007	2024-01-11	0	18	275985
\.


--
-- TOC entry 3642 (class 0 OID 275069)
-- Dependencies: 227
-- Data for Name: ticket; Type: TABLE DATA; Schema: public;
--

COPY public.ticket (id, coordinate_x, coordinate_y, creation_date, discount, name, price, type, event_id) FROM stdin;
10007	11	11	2024-01-11	11	275988	11	1	7
10016	23423	-234	2024-01-11	0	275998	1	2	6
10018	23423	-234	2024-01-11	0	276000	1	2	6
\.


--
-- TOC entry 3495 (class 2606 OID 275068)
-- Name: event event_pkey; Type: CONSTRAINT; Schema: public;
--

ALTER TABLE ONLY public.event
    ADD CONSTRAINT event_pkey PRIMARY KEY (id);


--
-- TOC entry 3497 (class 2606 OID 275073)
-- Name: ticket ticket_pkey; Type: CONSTRAINT; Schema: public;
--

ALTER TABLE ONLY public.ticket
    ADD CONSTRAINT ticket_pkey PRIMARY KEY (id);


--
-- TOC entry 3498 (class 2606 OID 275076)
-- Name: ticket fkfytuhjopeamxbt1cpudy92x5n; Type: FK CONSTRAINT; Schema: public;
--

ALTER TABLE ONLY public.ticket
    ADD CONSTRAINT fkfytuhjopeamxbt1cpudy92x5n FOREIGN KEY (event_id) REFERENCES public.event(id);


-- Completed on 2024-01-18 19:59:32 MSK

--
-- PostgreSQL database dump complete
--

