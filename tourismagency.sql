PGDMP  '    :                |            tourismagency    16.2    16.2 p    z           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            {           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            |           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            }           1262    17092    tourismagency    DATABASE     �   CREATE DATABASE tourismagency WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'English_United States.1252';
    DROP DATABASE tourismagency;
                postgres    false            u           1247    17364    authoritylevel    TYPE     W   CREATE TYPE public.authoritylevel AS ENUM (
    'Admin',
    'Manager',
    'Staff'
);
 !   DROP TYPE public.authoritylevel;
       public          postgres    false            �            1259    17280 	   amenities    TABLE     Z   CREATE TABLE public.amenities (
    amenity_id integer NOT NULL,
    amenity_name text
);
    DROP TABLE public.amenities;
       public         heap    postgres    false            �            1259    17279    amenities_amenity_id_seq    SEQUENCE     �   CREATE SEQUENCE public.amenities_amenity_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 /   DROP SEQUENCE public.amenities_amenity_id_seq;
       public          postgres    false    223            ~           0    0    amenities_amenity_id_seq    SEQUENCE OWNED BY     U   ALTER SEQUENCE public.amenities_amenity_id_seq OWNED BY public.amenities.amenity_id;
          public          postgres    false    222            �            1259    17306    discount_periods    TABLE     �   CREATE TABLE public.discount_periods (
    discount_id integer NOT NULL,
    hotel_id integer,
    start_date date,
    end_date date
);
 $   DROP TABLE public.discount_periods;
       public         heap    postgres    false            �            1259    17305     discount_periods_discount_id_seq    SEQUENCE     �   CREATE SEQUENCE public.discount_periods_discount_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 7   DROP SEQUENCE public.discount_periods_discount_id_seq;
       public          postgres    false    227                       0    0     discount_periods_discount_id_seq    SEQUENCE OWNED BY     e   ALTER SEQUENCE public.discount_periods_discount_id_seq OWNED BY public.discount_periods.discount_id;
          public          postgres    false    226            �            1259    17542    guest_types    TABLE     B  CREATE TABLE public.guest_types (
    guest_id integer NOT NULL,
    guest_type character varying(50) NOT NULL,
    CONSTRAINT chk_guest_id CHECK ((guest_id = ANY (ARRAY[1, 2]))),
    CONSTRAINT chk_guest_type CHECK (((guest_type)::text = ANY ((ARRAY['adult'::character varying, 'child'::character varying])::text[])))
);
    DROP TABLE public.guest_types;
       public         heap    postgres    false            �            1259    17541    guest_types_guest_id_seq    SEQUENCE     �   CREATE SEQUENCE public.guest_types_guest_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 /   DROP SEQUENCE public.guest_types_guest_id_seq;
       public          postgres    false    237            �           0    0    guest_types_guest_id_seq    SEQUENCE OWNED BY     U   ALTER SEQUENCE public.guest_types_guest_id_seq OWNED BY public.guest_types.guest_id;
          public          postgres    false    236            �            1259    17398    hotel_amenities    TABLE     h   CREATE TABLE public.hotel_amenities (
    hotel_id integer NOT NULL,
    amenity_id integer NOT NULL
);
 #   DROP TABLE public.hotel_amenities;
       public         heap    postgres    false            �            1259    17236    hotel_pensions    TABLE     g   CREATE TABLE public.hotel_pensions (
    hotel_id integer NOT NULL,
    pension_id integer NOT NULL
);
 "   DROP TABLE public.hotel_pensions;
       public         heap    postgres    false            �            1259    17481    hotel_room_features    TABLE     u   CREATE TABLE public.hotel_room_features (
    inventory_id integer NOT NULL,
    feature_type_id integer NOT NULL
);
 '   DROP TABLE public.hotel_room_features;
       public         heap    postgres    false            �            1259    17219    hotels    TABLE     �   CREATE TABLE public.hotels (
    hotel_id integer NOT NULL,
    hotel_name text NOT NULL,
    star_rating integer NOT NULL,
    city text NOT NULL,
    district text NOT NULL,
    email text NOT NULL,
    phone text NOT NULL,
    address text NOT NULL
);
    DROP TABLE public.hotels;
       public         heap    postgres    false            �            1259    17218    hotels_hotel_id_seq    SEQUENCE     �   CREATE SEQUENCE public.hotels_hotel_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 *   DROP SEQUENCE public.hotels_hotel_id_seq;
       public          postgres    false    216            �           0    0    hotels_hotel_id_seq    SEQUENCE OWNED BY     K   ALTER SEQUENCE public.hotels_hotel_id_seq OWNED BY public.hotels.hotel_id;
          public          postgres    false    215            �            1259    17228    pension_types    TABLE     ^   CREATE TABLE public.pension_types (
    pension_id integer NOT NULL,
    pension_type text
);
 !   DROP TABLE public.pension_types;
       public         heap    postgres    false            �            1259    17227    pension_types_pension_id_seq    SEQUENCE     �   CREATE SEQUENCE public.pension_types_pension_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 3   DROP SEQUENCE public.pension_types_pension_id_seq;
       public          postgres    false    218            �           0    0    pension_types_pension_id_seq    SEQUENCE OWNED BY     ]   ALTER SEQUENCE public.pension_types_pension_id_seq OWNED BY public.pension_types.pension_id;
          public          postgres    false    217            �            1259    17515    price    TABLE     �   CREATE TABLE public.price (
    price_id integer NOT NULL,
    hotel_id integer,
    room_type_id integer,
    pension_id integer,
    price_per_night numeric(10,2),
    discount_id integer,
    guest_id integer,
    inventory_id integer
);
    DROP TABLE public.price;
       public         heap    postgres    false            �            1259    17514    price_price_id_seq    SEQUENCE     �   CREATE SEQUENCE public.price_price_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 )   DROP SEQUENCE public.price_price_id_seq;
       public          postgres    false    235            �           0    0    price_price_id_seq    SEQUENCE OWNED BY     I   ALTER SEQUENCE public.price_price_id_seq OWNED BY public.price.price_id;
          public          postgres    false    234            �            1259    17692    reservations    TABLE     @  CREATE TABLE public.reservations (
    reservation_id integer NOT NULL,
    inventory_id integer NOT NULL,
    hotel_id integer NOT NULL,
    hotel_name text NOT NULL,
    discount_id integer NOT NULL,
    pension_id integer NOT NULL,
    room_type_id integer NOT NULL,
    child_count integer NOT NULL,
    adult_count integer NOT NULL,
    start_date date NOT NULL,
    end_date date NOT NULL,
    guest_name text NOT NULL,
    guest_phone text NOT NULL,
    guest_identification_number text NOT NULL,
    guest_email text NOT NULL,
    total_cost numeric(10,2) NOT NULL
);
     DROP TABLE public.reservations;
       public         heap    postgres    false            �            1259    17691    reservations_reservation_id_seq    SEQUENCE     �   CREATE SEQUENCE public.reservations_reservation_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 6   DROP SEQUENCE public.reservations_reservation_id_seq;
       public          postgres    false    239            �           0    0    reservations_reservation_id_seq    SEQUENCE OWNED BY     c   ALTER SEQUENCE public.reservations_reservation_id_seq OWNED BY public.reservations.reservation_id;
          public          postgres    false    238            �            1259    17289    room_feature_types    TABLE     q   CREATE TABLE public.room_feature_types (
    room_feature_id integer NOT NULL,
    feature_type text NOT NULL
);
 &   DROP TABLE public.room_feature_types;
       public         heap    postgres    false            �            1259    17288 "   room_amenities_room_amenity_id_seq    SEQUENCE     �   CREATE SEQUENCE public.room_amenities_room_amenity_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 9   DROP SEQUENCE public.room_amenities_room_amenity_id_seq;
       public          postgres    false    225            �           0    0 "   room_amenities_room_amenity_id_seq    SEQUENCE OWNED BY     m   ALTER SEQUENCE public.room_amenities_room_amenity_id_seq OWNED BY public.room_feature_types.room_feature_id;
          public          postgres    false    224            �            1259    17335    room_inventory    TABLE     �   CREATE TABLE public.room_inventory (
    inventory_id integer NOT NULL,
    hotel_id integer NOT NULL,
    quantity_available integer NOT NULL,
    room_type_id integer NOT NULL,
    room_size text NOT NULL,
    bed_count integer NOT NULL
);
 "   DROP TABLE public.room_inventory;
       public         heap    postgres    false            �            1259    17334    room_inventory_inventory_id_seq    SEQUENCE     �   CREATE SEQUENCE public.room_inventory_inventory_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 6   DROP SEQUENCE public.room_inventory_inventory_id_seq;
       public          postgres    false    229            �           0    0    room_inventory_inventory_id_seq    SEQUENCE OWNED BY     c   ALTER SEQUENCE public.room_inventory_inventory_id_seq OWNED BY public.room_inventory.inventory_id;
          public          postgres    false    228            �            1259    17252 
   room_types    TABLE     h   CREATE TABLE public.room_types (
    room_type_id integer NOT NULL,
    room_type_name text NOT NULL
);
    DROP TABLE public.room_types;
       public         heap    postgres    false            �            1259    17251    room_types_room_type_id_seq    SEQUENCE     �   CREATE SEQUENCE public.room_types_room_type_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 2   DROP SEQUENCE public.room_types_room_type_id_seq;
       public          postgres    false    221            �           0    0    room_types_room_type_id_seq    SEQUENCE OWNED BY     [   ALTER SEQUENCE public.room_types_room_type_id_seq OWNED BY public.room_types.room_type_id;
          public          postgres    false    220            �            1259    17383    users    TABLE     �   CREATE TABLE public.users (
    user_id integer NOT NULL,
    username text NOT NULL,
    password text NOT NULL,
    role text NOT NULL
);
    DROP TABLE public.users;
       public         heap    postgres    false            �            1259    17382    users_user_id_seq    SEQUENCE     �   CREATE SEQUENCE public.users_user_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 (   DROP SEQUENCE public.users_user_id_seq;
       public          postgres    false    231            �           0    0    users_user_id_seq    SEQUENCE OWNED BY     G   ALTER SEQUENCE public.users_user_id_seq OWNED BY public.users.user_id;
          public          postgres    false    230            �           2604    17283    amenities amenity_id    DEFAULT     |   ALTER TABLE ONLY public.amenities ALTER COLUMN amenity_id SET DEFAULT nextval('public.amenities_amenity_id_seq'::regclass);
 C   ALTER TABLE public.amenities ALTER COLUMN amenity_id DROP DEFAULT;
       public          postgres    false    222    223    223            �           2604    17309    discount_periods discount_id    DEFAULT     �   ALTER TABLE ONLY public.discount_periods ALTER COLUMN discount_id SET DEFAULT nextval('public.discount_periods_discount_id_seq'::regclass);
 K   ALTER TABLE public.discount_periods ALTER COLUMN discount_id DROP DEFAULT;
       public          postgres    false    227    226    227            �           2604    17545    guest_types guest_id    DEFAULT     |   ALTER TABLE ONLY public.guest_types ALTER COLUMN guest_id SET DEFAULT nextval('public.guest_types_guest_id_seq'::regclass);
 C   ALTER TABLE public.guest_types ALTER COLUMN guest_id DROP DEFAULT;
       public          postgres    false    237    236    237            �           2604    17222    hotels hotel_id    DEFAULT     r   ALTER TABLE ONLY public.hotels ALTER COLUMN hotel_id SET DEFAULT nextval('public.hotels_hotel_id_seq'::regclass);
 >   ALTER TABLE public.hotels ALTER COLUMN hotel_id DROP DEFAULT;
       public          postgres    false    216    215    216            �           2604    17231    pension_types pension_id    DEFAULT     �   ALTER TABLE ONLY public.pension_types ALTER COLUMN pension_id SET DEFAULT nextval('public.pension_types_pension_id_seq'::regclass);
 G   ALTER TABLE public.pension_types ALTER COLUMN pension_id DROP DEFAULT;
       public          postgres    false    218    217    218            �           2604    17518    price price_id    DEFAULT     p   ALTER TABLE ONLY public.price ALTER COLUMN price_id SET DEFAULT nextval('public.price_price_id_seq'::regclass);
 =   ALTER TABLE public.price ALTER COLUMN price_id DROP DEFAULT;
       public          postgres    false    235    234    235            �           2604    17695    reservations reservation_id    DEFAULT     �   ALTER TABLE ONLY public.reservations ALTER COLUMN reservation_id SET DEFAULT nextval('public.reservations_reservation_id_seq'::regclass);
 J   ALTER TABLE public.reservations ALTER COLUMN reservation_id DROP DEFAULT;
       public          postgres    false    239    238    239            �           2604    17292 "   room_feature_types room_feature_id    DEFAULT     �   ALTER TABLE ONLY public.room_feature_types ALTER COLUMN room_feature_id SET DEFAULT nextval('public.room_amenities_room_amenity_id_seq'::regclass);
 Q   ALTER TABLE public.room_feature_types ALTER COLUMN room_feature_id DROP DEFAULT;
       public          postgres    false    225    224    225            �           2604    17338    room_inventory inventory_id    DEFAULT     �   ALTER TABLE ONLY public.room_inventory ALTER COLUMN inventory_id SET DEFAULT nextval('public.room_inventory_inventory_id_seq'::regclass);
 J   ALTER TABLE public.room_inventory ALTER COLUMN inventory_id DROP DEFAULT;
       public          postgres    false    229    228    229            �           2604    17255    room_types room_type_id    DEFAULT     �   ALTER TABLE ONLY public.room_types ALTER COLUMN room_type_id SET DEFAULT nextval('public.room_types_room_type_id_seq'::regclass);
 F   ALTER TABLE public.room_types ALTER COLUMN room_type_id DROP DEFAULT;
       public          postgres    false    220    221    221            �           2604    17386    users user_id    DEFAULT     n   ALTER TABLE ONLY public.users ALTER COLUMN user_id SET DEFAULT nextval('public.users_user_id_seq'::regclass);
 <   ALTER TABLE public.users ALTER COLUMN user_id DROP DEFAULT;
       public          postgres    false    231    230    231            g          0    17280 	   amenities 
   TABLE DATA           =   COPY public.amenities (amenity_id, amenity_name) FROM stdin;
    public          postgres    false    223   p�       k          0    17306    discount_periods 
   TABLE DATA           W   COPY public.discount_periods (discount_id, hotel_id, start_date, end_date) FROM stdin;
    public          postgres    false    227   �       u          0    17542    guest_types 
   TABLE DATA           ;   COPY public.guest_types (guest_id, guest_type) FROM stdin;
    public          postgres    false    237   e�       p          0    17398    hotel_amenities 
   TABLE DATA           ?   COPY public.hotel_amenities (hotel_id, amenity_id) FROM stdin;
    public          postgres    false    232   ��       c          0    17236    hotel_pensions 
   TABLE DATA           >   COPY public.hotel_pensions (hotel_id, pension_id) FROM stdin;
    public          postgres    false    219   �       q          0    17481    hotel_room_features 
   TABLE DATA           L   COPY public.hotel_room_features (inventory_id, feature_type_id) FROM stdin;
    public          postgres    false    233   3�       `          0    17219    hotels 
   TABLE DATA           j   COPY public.hotels (hotel_id, hotel_name, star_rating, city, district, email, phone, address) FROM stdin;
    public          postgres    false    216   }�       b          0    17228    pension_types 
   TABLE DATA           A   COPY public.pension_types (pension_id, pension_type) FROM stdin;
    public          postgres    false    218   0�       s          0    17515    price 
   TABLE DATA           �   COPY public.price (price_id, hotel_id, room_type_id, pension_id, price_per_night, discount_id, guest_id, inventory_id) FROM stdin;
    public          postgres    false    235   ��       w          0    17692    reservations 
   TABLE DATA              COPY public.reservations (reservation_id, inventory_id, hotel_id, hotel_name, discount_id, pension_id, room_type_id, child_count, adult_count, start_date, end_date, guest_name, guest_phone, guest_identification_number, guest_email, total_cost) FROM stdin;
    public          postgres    false    239   :�       i          0    17289    room_feature_types 
   TABLE DATA           K   COPY public.room_feature_types (room_feature_id, feature_type) FROM stdin;
    public          postgres    false    225   $�       m          0    17335    room_inventory 
   TABLE DATA           x   COPY public.room_inventory (inventory_id, hotel_id, quantity_available, room_type_id, room_size, bed_count) FROM stdin;
    public          postgres    false    229   |�       e          0    17252 
   room_types 
   TABLE DATA           B   COPY public.room_types (room_type_id, room_type_name) FROM stdin;
    public          postgres    false    221   ˓       o          0    17383    users 
   TABLE DATA           B   COPY public.users (user_id, username, password, role) FROM stdin;
    public          postgres    false    231   �       �           0    0    amenities_amenity_id_seq    SEQUENCE SET     F   SELECT pg_catalog.setval('public.amenities_amenity_id_seq', 7, true);
          public          postgres    false    222            �           0    0     discount_periods_discount_id_seq    SEQUENCE SET     O   SELECT pg_catalog.setval('public.discount_periods_discount_id_seq', 53, true);
          public          postgres    false    226            �           0    0    guest_types_guest_id_seq    SEQUENCE SET     G   SELECT pg_catalog.setval('public.guest_types_guest_id_seq', 1, false);
          public          postgres    false    236            �           0    0    hotels_hotel_id_seq    SEQUENCE SET     B   SELECT pg_catalog.setval('public.hotels_hotel_id_seq', 27, true);
          public          postgres    false    215            �           0    0    pension_types_pension_id_seq    SEQUENCE SET     J   SELECT pg_catalog.setval('public.pension_types_pension_id_seq', 7, true);
          public          postgres    false    217            �           0    0    price_price_id_seq    SEQUENCE SET     A   SELECT pg_catalog.setval('public.price_price_id_seq', 32, true);
          public          postgres    false    234            �           0    0    reservations_reservation_id_seq    SEQUENCE SET     N   SELECT pg_catalog.setval('public.reservations_reservation_id_seq', 18, true);
          public          postgres    false    238            �           0    0 "   room_amenities_room_amenity_id_seq    SEQUENCE SET     P   SELECT pg_catalog.setval('public.room_amenities_room_amenity_id_seq', 5, true);
          public          postgres    false    224            �           0    0    room_inventory_inventory_id_seq    SEQUENCE SET     N   SELECT pg_catalog.setval('public.room_inventory_inventory_id_seq', 19, true);
          public          postgres    false    228            �           0    0    room_types_room_type_id_seq    SEQUENCE SET     I   SELECT pg_catalog.setval('public.room_types_room_type_id_seq', 4, true);
          public          postgres    false    220            �           0    0    users_user_id_seq    SEQUENCE SET     @   SELECT pg_catalog.setval('public.users_user_id_seq', 10, true);
          public          postgres    false    230            �           2606    17287    amenities amenities_pkey 
   CONSTRAINT     ^   ALTER TABLE ONLY public.amenities
    ADD CONSTRAINT amenities_pkey PRIMARY KEY (amenity_id);
 B   ALTER TABLE ONLY public.amenities DROP CONSTRAINT amenities_pkey;
       public            postgres    false    223            �           2606    17311 &   discount_periods discount_periods_pkey 
   CONSTRAINT     m   ALTER TABLE ONLY public.discount_periods
    ADD CONSTRAINT discount_periods_pkey PRIMARY KEY (discount_id);
 P   ALTER TABLE ONLY public.discount_periods DROP CONSTRAINT discount_periods_pkey;
       public            postgres    false    227            �           2606    17549    guest_types guest_types_pkey 
   CONSTRAINT     `   ALTER TABLE ONLY public.guest_types
    ADD CONSTRAINT guest_types_pkey PRIMARY KEY (guest_id);
 F   ALTER TABLE ONLY public.guest_types DROP CONSTRAINT guest_types_pkey;
       public            postgres    false    237            �           2606    17240 "   hotel_pensions hotel_pensions_pkey 
   CONSTRAINT     r   ALTER TABLE ONLY public.hotel_pensions
    ADD CONSTRAINT hotel_pensions_pkey PRIMARY KEY (hotel_id, pension_id);
 L   ALTER TABLE ONLY public.hotel_pensions DROP CONSTRAINT hotel_pensions_pkey;
       public            postgres    false    219    219            �           2606    17594 ,   hotel_room_features hotel_room_features_pkey 
   CONSTRAINT     �   ALTER TABLE ONLY public.hotel_room_features
    ADD CONSTRAINT hotel_room_features_pkey PRIMARY KEY (inventory_id, feature_type_id);
 V   ALTER TABLE ONLY public.hotel_room_features DROP CONSTRAINT hotel_room_features_pkey;
       public            postgres    false    233    233            �           2606    17402 !   hotel_amenities hotelamenity_pkey 
   CONSTRAINT     q   ALTER TABLE ONLY public.hotel_amenities
    ADD CONSTRAINT hotelamenity_pkey PRIMARY KEY (hotel_id, amenity_id);
 K   ALTER TABLE ONLY public.hotel_amenities DROP CONSTRAINT hotelamenity_pkey;
       public            postgres    false    232    232            �           2606    17226    hotels hotels_pkey 
   CONSTRAINT     V   ALTER TABLE ONLY public.hotels
    ADD CONSTRAINT hotels_pkey PRIMARY KEY (hotel_id);
 <   ALTER TABLE ONLY public.hotels DROP CONSTRAINT hotels_pkey;
       public            postgres    false    216            �           2606    17235     pension_types pension_types_pkey 
   CONSTRAINT     f   ALTER TABLE ONLY public.pension_types
    ADD CONSTRAINT pension_types_pkey PRIMARY KEY (pension_id);
 J   ALTER TABLE ONLY public.pension_types DROP CONSTRAINT pension_types_pkey;
       public            postgres    false    218            �           2606    17520    price price_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.price
    ADD CONSTRAINT price_pkey PRIMARY KEY (price_id);
 :   ALTER TABLE ONLY public.price DROP CONSTRAINT price_pkey;
       public            postgres    false    235            �           2606    17699    reservations reservations_pkey 
   CONSTRAINT     h   ALTER TABLE ONLY public.reservations
    ADD CONSTRAINT reservations_pkey PRIMARY KEY (reservation_id);
 H   ALTER TABLE ONLY public.reservations DROP CONSTRAINT reservations_pkey;
       public            postgres    false    239            �           2606    17294 &   room_feature_types room_amenities_pkey 
   CONSTRAINT     q   ALTER TABLE ONLY public.room_feature_types
    ADD CONSTRAINT room_amenities_pkey PRIMARY KEY (room_feature_id);
 P   ALTER TABLE ONLY public.room_feature_types DROP CONSTRAINT room_amenities_pkey;
       public            postgres    false    225            �           2606    17340 "   room_inventory room_inventory_pkey 
   CONSTRAINT     j   ALTER TABLE ONLY public.room_inventory
    ADD CONSTRAINT room_inventory_pkey PRIMARY KEY (inventory_id);
 L   ALTER TABLE ONLY public.room_inventory DROP CONSTRAINT room_inventory_pkey;
       public            postgres    false    229            �           2606    17259    room_types room_types_pkey 
   CONSTRAINT     b   ALTER TABLE ONLY public.room_types
    ADD CONSTRAINT room_types_pkey PRIMARY KEY (room_type_id);
 D   ALTER TABLE ONLY public.room_types DROP CONSTRAINT room_types_pkey;
       public            postgres    false    221            �           2606    17390    users users_pkey 
   CONSTRAINT     S   ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (user_id);
 :   ALTER TABLE ONLY public.users DROP CONSTRAINT users_pkey;
       public            postgres    false    231            �           2606    17392    users users_username_key 
   CONSTRAINT     W   ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_username_key UNIQUE (username);
 B   ALTER TABLE ONLY public.users DROP CONSTRAINT users_username_key;
       public            postgres    false    231            �           2606    17468 /   discount_periods discount_periods_hotel_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.discount_periods
    ADD CONSTRAINT discount_periods_hotel_id_fkey FOREIGN KEY (hotel_id) REFERENCES public.hotels(hotel_id) ON DELETE CASCADE;
 Y   ALTER TABLE ONLY public.discount_periods DROP CONSTRAINT discount_periods_hotel_id_fkey;
       public          postgres    false    216    4767    227            �           2606    17423    hotel_amenities fk_amenity_id    FK CONSTRAINT     �   ALTER TABLE ONLY public.hotel_amenities
    ADD CONSTRAINT fk_amenity_id FOREIGN KEY (amenity_id) REFERENCES public.amenities(amenity_id) ON DELETE CASCADE;
 G   ALTER TABLE ONLY public.hotel_amenities DROP CONSTRAINT fk_amenity_id;
       public          postgres    false    232    223    4775            �           2606    17600 &   hotel_room_features fk_feature_type_id    FK CONSTRAINT     �   ALTER TABLE ONLY public.hotel_room_features
    ADD CONSTRAINT fk_feature_type_id FOREIGN KEY (feature_type_id) REFERENCES public.room_feature_types(room_feature_id) ON DELETE CASCADE;
 P   ALTER TABLE ONLY public.hotel_room_features DROP CONSTRAINT fk_feature_type_id;
       public          postgres    false    225    233    4777            �           2606    17550    price fk_guest_id    FK CONSTRAINT     }   ALTER TABLE ONLY public.price
    ADD CONSTRAINT fk_guest_id FOREIGN KEY (guest_id) REFERENCES public.guest_types(guest_id);
 ;   ALTER TABLE ONLY public.price DROP CONSTRAINT fk_guest_id;
       public          postgres    false    235    237    4793            �           2606    17413    hotel_pensions fk_hotel_id    FK CONSTRAINT     �   ALTER TABLE ONLY public.hotel_pensions
    ADD CONSTRAINT fk_hotel_id FOREIGN KEY (hotel_id) REFERENCES public.hotels(hotel_id) ON DELETE CASCADE;
 D   ALTER TABLE ONLY public.hotel_pensions DROP CONSTRAINT fk_hotel_id;
       public          postgres    false    4767    216    219            �           2606    17428    hotel_amenities fk_hotel_id    FK CONSTRAINT     �   ALTER TABLE ONLY public.hotel_amenities
    ADD CONSTRAINT fk_hotel_id FOREIGN KEY (hotel_id) REFERENCES public.hotels(hotel_id) ON DELETE CASCADE;
 E   ALTER TABLE ONLY public.hotel_amenities DROP CONSTRAINT fk_hotel_id;
       public          postgres    false    232    4767    216            �           2606    17595 #   hotel_room_features fk_inventory_id    FK CONSTRAINT     �   ALTER TABLE ONLY public.hotel_room_features
    ADD CONSTRAINT fk_inventory_id FOREIGN KEY (inventory_id) REFERENCES public.room_inventory(inventory_id) ON DELETE CASCADE;
 M   ALTER TABLE ONLY public.hotel_room_features DROP CONSTRAINT fk_inventory_id;
       public          postgres    false    233    229    4781            �           2606    17605    price fk_inventory_id    FK CONSTRAINT     �   ALTER TABLE ONLY public.price
    ADD CONSTRAINT fk_inventory_id FOREIGN KEY (inventory_id) REFERENCES public.room_inventory(inventory_id) ON DELETE CASCADE;
 ?   ALTER TABLE ONLY public.price DROP CONSTRAINT fk_inventory_id;
       public          postgres    false    229    4781    235            �           2606    17418    hotel_pensions fk_pension_id    FK CONSTRAINT     �   ALTER TABLE ONLY public.hotel_pensions
    ADD CONSTRAINT fk_pension_id FOREIGN KEY (pension_id) REFERENCES public.pension_types(pension_id) ON DELETE CASCADE;
 F   ALTER TABLE ONLY public.hotel_pensions DROP CONSTRAINT fk_pension_id;
       public          postgres    false    219    4769    218            �           2606    17555    room_inventory fk_room_type_id    FK CONSTRAINT     �   ALTER TABLE ONLY public.room_inventory
    ADD CONSTRAINT fk_room_type_id FOREIGN KEY (room_type_id) REFERENCES public.room_types(room_type_id) ON DELETE CASCADE;
 H   ALTER TABLE ONLY public.room_inventory DROP CONSTRAINT fk_room_type_id;
       public          postgres    false    221    229    4773            �           2606    17536    price price_discount_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.price
    ADD CONSTRAINT price_discount_id_fkey FOREIGN KEY (discount_id) REFERENCES public.discount_periods(discount_id) ON DELETE CASCADE;
 F   ALTER TABLE ONLY public.price DROP CONSTRAINT price_discount_id_fkey;
       public          postgres    false    227    235    4779            �           2606    17521    price price_hotel_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.price
    ADD CONSTRAINT price_hotel_id_fkey FOREIGN KEY (hotel_id) REFERENCES public.hotels(hotel_id) ON DELETE CASCADE;
 C   ALTER TABLE ONLY public.price DROP CONSTRAINT price_hotel_id_fkey;
       public          postgres    false    4767    216    235            �           2606    17531    price price_pension_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.price
    ADD CONSTRAINT price_pension_id_fkey FOREIGN KEY (pension_id) REFERENCES public.pension_types(pension_id) ON DELETE CASCADE;
 E   ALTER TABLE ONLY public.price DROP CONSTRAINT price_pension_id_fkey;
       public          postgres    false    4769    218    235            �           2606    17526    price price_room_type_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.price
    ADD CONSTRAINT price_room_type_id_fkey FOREIGN KEY (room_type_id) REFERENCES public.room_types(room_type_id) ON DELETE CASCADE;
 G   ALTER TABLE ONLY public.price DROP CONSTRAINT price_room_type_id_fkey;
       public          postgres    false    221    235    4773            �           2606    17715 *   reservations reservations_discount_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.reservations
    ADD CONSTRAINT reservations_discount_id_fkey FOREIGN KEY (discount_id) REFERENCES public.discount_periods(discount_id) ON DELETE CASCADE;
 T   ALTER TABLE ONLY public.reservations DROP CONSTRAINT reservations_discount_id_fkey;
       public          postgres    false    4779    239    227            �           2606    17705 '   reservations reservations_hotel_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.reservations
    ADD CONSTRAINT reservations_hotel_id_fkey FOREIGN KEY (hotel_id) REFERENCES public.hotels(hotel_id) ON DELETE CASCADE;
 Q   ALTER TABLE ONLY public.reservations DROP CONSTRAINT reservations_hotel_id_fkey;
       public          postgres    false    216    239    4767            �           2606    17700 +   reservations reservations_inventory_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.reservations
    ADD CONSTRAINT reservations_inventory_id_fkey FOREIGN KEY (inventory_id) REFERENCES public.room_inventory(inventory_id) ON DELETE CASCADE;
 U   ALTER TABLE ONLY public.reservations DROP CONSTRAINT reservations_inventory_id_fkey;
       public          postgres    false    239    4781    229            �           2606    17720 )   reservations reservations_pension_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.reservations
    ADD CONSTRAINT reservations_pension_id_fkey FOREIGN KEY (pension_id) REFERENCES public.pension_types(pension_id) ON DELETE CASCADE;
 S   ALTER TABLE ONLY public.reservations DROP CONSTRAINT reservations_pension_id_fkey;
       public          postgres    false    4769    239    218            �           2606    17725 +   reservations reservations_room_type_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.reservations
    ADD CONSTRAINT reservations_room_type_id_fkey FOREIGN KEY (room_type_id) REFERENCES public.room_types(room_type_id) ON DELETE CASCADE;
 U   ALTER TABLE ONLY public.reservations DROP CONSTRAINT reservations_room_type_id_fkey;
       public          postgres    false    4773    239    221            �           2606    17570 +   room_inventory room_inventory_hotel_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.room_inventory
    ADD CONSTRAINT room_inventory_hotel_id_fkey FOREIGN KEY (hotel_id) REFERENCES public.hotels(hotel_id) ON DELETE CASCADE;
 U   ALTER TABLE ONLY public.room_inventory DROP CONSTRAINT room_inventory_hotel_id_fkey;
       public          postgres    false    216    4767    229            g   h   x�%�1
�@�z�)��1�{ S�؄�ݝe6��+ؽ�$�m���g�?Vʏ��߸��k6�J\enFI�U���uc`�a<Nzw/��eb��K�����(      k   m   x�u�K�0C�pF�O�ܥ�?G�L�HSugx�ddj!�D�0q�'�O��4��m6�ra�6Y��-S��ᛡ��V�$�������\�9�f��T5�=-3r�������L+u      u      x�3�LL)�)�2�L���I����� @�P      p   J   x�%���0C�x���2L����r�I���V�1X'hl��ݐ�xu**�B�ʹ�+��GMam(h}����U�      c   7   x�ȱ�0���9L�Wl��d�9"5�.�
��!�ݯI|�!ּ�ML���q[      q   :   x�%��  ��c�+���B|0!����L�"������v>.R�� �
�      `   �  x�=��n�0���S���v�.���vC�K�]v�m6�&��H'��ON�Bҁ¯��?��N�/b�a{
�)tN;�g�=|&c��d�ȴ�d�b]��,ʪ��󮓀/쎃iV|�'�#�T�ެر�@����s��B�l�h�j\�ն�+x<qp=��Evt��y��͊�.�[t�.�k8H�`;��{��!�$��H���"O�(�U"��&�G��K�Vf�'�}V& s��ƃ�9&";G����6`���7Im��j����oSP�3������y�w4�'�c�bN��ЈN��Y�<u�X}Q���V��H��/����Ҷ��R�<�CꤓXjI�i�;S��,����;$��g��('�88�5+sh�1��z��K��A�F�hݱn9�l�˂�.�*�G/q����?r�s�e�?h���      b   w   x�3��))JTp��Q��K�)-�,K�2�t-K-�,���K����ps���*8�&f�%�p�p�����g��q�rz$���f��y9�\���E�)�%
~�@���3�s�b���� ~�*�      s   s   x�M���0�P�IL/�_���b�eo�2R��"�3��*;��*G��#���
�o�.� ���@�ir�Z�Y�_��̦N��['��V ��yC�Gi��Ɇ"�Hp�|"$q      w   �   x�E��j�0E��_����#�v^�JK���(4Ԏ��:�ߑ��Ņ3���B[�/���%��t��aI4i�aڰ^��5�nS���Y{	���эK�>��l�&�ء��K��ڥ1�`/"Sd�hV�؏��ާ�O}�Ð��D�Kp+�.-����bl�x���c>_���z����dV���s>�A=š��R��	�,;�,s�뫮��̴RO      i   H   x�3�����LJ,�2�tO���KWH��+��I�2�NLK�2�(��JM.�/�2�I�I-�,�������� ���      m   ?   x���� ��^1g���:b�#�_�O�MpH� ��Z�Uɇ�]#�z���d�I?��
N      e   9   x�3���K�IU(����2�t�/M�@<cN�Ҽ��"���̒T.�`0���� �U�      o   [   x�-�A
�0�γ�A����N�j-�E����kH�c׃�S�Е�I�L8>\��pɀ�>e+�fw-L˙2�7x��z��8�*N��|[1"     